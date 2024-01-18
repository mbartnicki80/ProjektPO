package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.PositionOutOfBoundsException;

import java.util.*;
import java.util.stream.Stream;

public class Simulation implements Runnable {

    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final WorldMap worldMap;
    private final int plantsPerDay;
    private final int reproductionReadyEnergy;
    private final int usedReproductionEnergy;
    private boolean isRunning = true;

    public Simulation(WorldMap worldMap, int numberOfAnimals, int startAnimalEnergy,
                      int plantsPerDay, int reproductionReadyEnergy, int usedReproductionEnergy,
                      int minimalMutations, int maximalMutations, int genomeLength, boolean fullRandomnessGenome) {

        this.worldMap = worldMap;
        this.plantsPerDay = plantsPerDay;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.usedReproductionEnergy = usedReproductionEnergy;
        AnimalFactory animalFactory = new AnimalFactory(genomeLength, minimalMutations, maximalMutations, fullRandomnessGenome);
        worldMap.setAnimalFactory(animalFactory);
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
            Animal animal = AnimalFactory.create(position, startAnimalEnergy, 0, genomeLength);
            worldMap.place(animal);
            aliveAnimals.add(animal);
        }
    }

    public void run() {

        try {
            int day = 0;
            while (!aliveAnimals.isEmpty()) {
                if (isRunning) {

                    removeDeadAnimals();
                    moveAnimals();
                    consumption();
                    reproduceAnimals();
                    growNewPlants();

                    if (this.worldMap instanceof LifeGivingCorpses lifeGivingCorpses) {
                        lifeGivingCorpses.update(day);
                    }
                    worldMap.dayUpdate();
                    day++;
                    Thread.sleep(500);
                }
                else
                    Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }

    private void removeDeadAnimals() {
        Iterator<Animal> iterator = aliveAnimals.iterator();

        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.isDead()) {
                worldMap.removeDeadAnimal(animal);
                iterator.remove();
            }
        }
    }

    private void moveAnimals() {
        for (Animal animal : aliveAnimals) {
            try {
                worldMap.move(animal);
            }
            catch (PositionOutOfBoundsException ignored) {}
        }
    }

    private void consumption() {
        worldMap.consumption();
    }

    private void reproduceAnimals() {
        List<Animal> newbornAnimals = worldMap
                .reproduceAnimals(reproductionReadyEnergy, usedReproductionEnergy);
        aliveAnimals.addAll(newbornAnimals);
    }

    private void growNewPlants() {
        worldMap.growNewPlants(plantsPerDay);
    }

    public void changeRunningMode() {
        this.isRunning = !this.isRunning;
    }
}
