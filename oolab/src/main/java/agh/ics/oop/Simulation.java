package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.*;
import java.util.stream.Stream;

public class Simulation implements Runnable {

    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final WorldMap worldMap;
    private final int plantsPerDay;
    private final int reproductionReadyEnergy;
    private final int usedReproductionEnergy;
    private final int minimalMutations;
    private final int maximalMutations;
    private final int genomeLength;
    private final boolean fullRandomnessGenome;
    private boolean isRunning = true;
    private static final MapDirection[] directions = MapDirection.values();

    public Simulation(WorldMap worldMap, int numberOfAnimals, int startAnimalEnergy,
                      int plantsPerDay, int reproductionReadyEnergy, int usedReproductionEnergy,
                      int minimalMutations, int maximalMutations, int genomeLength, boolean fullRandomnessGenome) {

        this.worldMap = worldMap;
        this.plantsPerDay = plantsPerDay;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.usedReproductionEnergy = usedReproductionEnergy;
        this.minimalMutations = minimalMutations;
        this.maximalMutations = maximalMutations;
        this.genomeLength = genomeLength;
        this.fullRandomnessGenome = fullRandomnessGenome;

        Random random = new Random();
        Boundary boundary = worldMap.getCurrentBounds();

        List<Vector2d> positions = Stream.generate(() ->
                        new Vector2d(
                                random.nextInt(boundary.upperRight().getXValue()),
                                random.nextInt(boundary.upperRight().getYValue())
                        )
                )
                .limit(numberOfAnimals)
                .toList();

        for (Vector2d position : positions) {
            MapDirection direction = directions[random.nextInt(directions.length)];

            Animal animal = new Animal(position, direction, startAnimalEnergy, 0, genomeLength);
            worldMap.place(animal);
            aliveAnimals.add(animal);
        }
    }

    public void run() {

        try {
            int day = 0;
            while (!aliveAnimals.isEmpty()) {
                if (isRunning) {
                    removeDeadAnimals(day);
                    moveAnimals();
                    consumption();
                    reproduceAnimals(day);
                    growNewPlants();

                    day++;
                    Thread.sleep(500);
                }
                else
                    Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }

    private void removeDeadAnimals(int day) {
        Iterator<Animal> iterator = aliveAnimals.iterator();

        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.isDead()) {
                worldMap.removeDeadAnimal(animal, day);
                iterator.remove();
            }
        }
    }

    private void moveAnimals() {
        for (Animal animal : aliveAnimals) {
            worldMap.move(animal);
        }
    }

    private void consumption() {
        worldMap.consumption();
    }

    private void reproduceAnimals(int day) {
        List<Animal> newbornAnimals = worldMap
                .reproduceAnimals(
                        day,
                        genomeLength,
                        minimalMutations,
                        maximalMutations,
                        reproductionReadyEnergy,
                        usedReproductionEnergy,
                        fullRandomnessGenome);
        aliveAnimals.addAll(newbornAnimals);
    }

    private void growNewPlants() {
        worldMap.growNewPlants(plantsPerDay);
    }

    public void changeRunningMode() {
        this.isRunning = !this.isRunning;
    }
}
