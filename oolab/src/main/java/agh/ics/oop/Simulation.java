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
    private final boolean fullRandomnessGenome;
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

                removeDeadAnimals();
                moveAnimals();
                consumption();
                reproduceAnimals(day);
                growNewPlants();

                day++;
                Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }

    private void removeDeadAnimals() {
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
            //nie wiem jak to rozegrac, ale zbey byl interfejs, to musza miec takie same argumenty
            //a inaczej niz bez interfejsu to nie pociagnie, bo nie wykryje nawet istnienia tych metod

            //no a tutaj musimy przekazac jakos wyzej nasze deadAnimalsy
            //chyba ze przechowywac deadAnimalsy tez w worldMapie

            //moim pomyslem jest zeby stworzyc interfejs na generatory, mialby jedna metode
            //generatePreferedPositions
            //i to by byla jakas iterable klasa co to implementuje
            //i trzeba by tutaj tego seta pozmieniac na te gowna
        worldMap.growNewPlants(plantsPerDay);
    }
}
