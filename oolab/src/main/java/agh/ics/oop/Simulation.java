package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.*;
import java.util.stream.Stream;

public class Simulation implements Runnable {

    private final List<Animal> aliveAnimals = new ArrayList<>();
    private final HashMap<Vector2d, Animal> deadAnimals = new HashMap<>(); //przerobilem na hashmape, bo przydadza sie pozycje do zyciodajnych truchl
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

        MapVisualizer visualizer = new MapVisualizer(worldMap);
    }

    public void run() {
        /* 1. Wygenerowanie początkowych zwierzaków
        * (rośliny już są generowane przy tworzeniu mapy
        *  */
        //przeniosłem do inicjalizacji - wydaje mi sie, ze tam bardziej pasuje, gdyz zwierzaki
        //powstaja w momencie tworzenia symulacji, a run odpowiada tylko za kolejne kroki symulacji

        //trzeba tutaj dorobic jakis licznik, aktualnie i przeksztalcilem na day
        try {
            for (int day = 0; day < 10; day++) {
                removeDeadAnimals();
                moveAnimals();
                consumption();
                reproduceAnimals(day);
                //5. Wzrastanie nowych roślin na wybranych polach mapy.
                growNewPlants();

                Thread.sleep(500);

            }
        } catch (InterruptedException ignored) {}
    }

        // #TODO
        //5. Wzrastanie nowych roślin na wybranych polach mapy.


    //nie dajemy tutaj zadnych private/public? zostawiamy package-private?

    void removeDeadAnimals() {
        for (Animal animal : aliveAnimals) {
            if (animal.isDead()) {
                deadAnimals.put(animal.getPosition(), animal);
                worldMap.remove(animal);
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
        List<Animal> newbornAnimals = worldMap.reproduceAnimals(day, genomeLength, minimalMutations, maximalMutations,
                                                                reproductionReadyEnergy, usedReproductionEnergy);
        aliveAnimals.addAll(newbornAnimals);
    }

    void growNewPlants() {
        worldMap.growNewPlants(plantsPerDay);
    }
}
