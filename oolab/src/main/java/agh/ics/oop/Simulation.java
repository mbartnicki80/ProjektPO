package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.*;
import java.util.stream.Stream;

public class Simulation implements Runnable {

    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final Map<Vector2d, Animal> deadAnimals = new HashMap<>();
    private final WorldMap worldMap;
    private final int plantsPerDay;
    private final int reproductionReadyEnergy;
    private final int usedReproductionEnergy;
    private final int minimalMutations;
    private final int maximalMutations;
    private final int genomeLength;
    private static final MapDirection[] directions = MapDirection.values();
    private final int plantEnergy;

    public Simulation(WorldMap worldMap, int numberOfAnimals, int startAnimalEnergy,
            int plantEnergy, int plantsPerDay, int reproductionReadyEnergy,
            int usedReproductionEnergy, int minimalMutations, int maximalMutations, int genomeLength) {

        this.worldMap = worldMap;
        this.plantEnergy = plantEnergy;
        this.plantsPerDay = plantsPerDay;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.usedReproductionEnergy = usedReproductionEnergy;
        this.minimalMutations = minimalMutations;
        this.maximalMutations = maximalMutations;
        this.genomeLength = genomeLength;

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

        //trzeba tutaj dorobic jakis licznik, aktualnie i przeksztalcilem na day
        try {
            int day = 0;
            while (!aliveAnimals.isEmpty()) {

                /* Done */
                removeDeadAnimals();

                /* Done */
                moveAnimals();

                /* Done */
                consumption();

                /* Done */
                reproduceAnimals(day);

                /* Done */
                growNewPlants();

                Thread.sleep(500);

                day++;
            }
        } catch (InterruptedException ignored) {}
    }

        // #TODO
        //5. Wzrastanie nowych roślin na wybranych polach mapy.


    //nie dajemy tutaj zadnych private/public? zostawiamy package-private?

    void removeDeadAnimals() {
        Iterator<Animal> iterator = aliveAnimals.iterator();

        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.isDead()) {
                deadAnimals.put(animal.position(), animal);
                worldMap.remove(animal);
                iterator.remove();
            }
        }
    }

    void moveAnimals() {
        for (Animal animal : aliveAnimals) {
            worldMap.move(animal);
        }
    }

    void consumption() {
        worldMap.consumption(this.plantEnergy);
    }

    void reproduceAnimals(int day) {
        List<Animal> newbornAnimals = worldMap
                .reproduceAnimals(
                        day,
                        genomeLength,
                        minimalMutations,
                        maximalMutations,
                        reproductionReadyEnergy,
                        usedReproductionEnergy);
        aliveAnimals.addAll(newbornAnimals);
    }

    void growNewPlants() {
        worldMap.growNewPlants(plantsPerDay);
    }
}
