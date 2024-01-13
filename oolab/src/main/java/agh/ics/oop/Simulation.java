package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.*;
import java.util.stream.Stream;

public class Simulation implements Runnable {

    private final List<Animal> aliveAnimals;
    private final WorldMap worldMap;

    private final int plantsPerDay;

    private final int reproductionReadyEnergy;
    private final int usedReproductionEnergy;
    private final int minimalMutations;
    private final int maximalMutations;
    private final int genomeLength;
    private static final MapDirection[] directions = MapDirection.values();

    private int animalCount;
    private int startAnimalEnergy;

    private int plantEnergy;

    public Simulation(
            WorldMap worldMap,
            int numberOfAnimals,
            int startAnimalEnergy,
            int plantEnergy,
            int plantsPerDay,
            int reproductionReadyEnergy,
            int usedReproductionEnergy,
            int minimalMutations,
            int maximalMutations,
            int genomeLength) {

        this.worldMap = worldMap;
        this.animalCount = numberOfAnimals;
        this.startAnimalEnergy = startAnimalEnergy;
        this.plantEnergy = plantEnergy;
        this.plantsPerDay = plantsPerDay;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.usedReproductionEnergy = usedReproductionEnergy;
        this.minimalMutations = minimalMutations;
        this.maximalMutations = maximalMutations;
        this.genomeLength = genomeLength;

        aliveAnimals = new ArrayList<>(animalCount);
    }

    public void run() {

        /* 1. Wygenerowanie początkowych zwierzaków
        * (rośliny już są generowane przy tworzeniu mapy
        *  */
        Random random = new Random();
        Boundary boundary = worldMap.getCurrentBounds();

        List<Vector2d> positions = Stream.generate(() ->
                        new Vector2d(
                                random.nextInt(boundary.upperRight().getXValue()),
                                random.nextInt(boundary.upperRight().getYValue())
                        )
                )
                .limit(animalCount)
                .toList();

        for (Vector2d position : positions) {
            MapDirection direction = directions[random.nextInt(directions.length)];

            Animal animal = new Animal(position, direction, startAnimalEnergy, 0, genomeLength);
            worldMap.place(animal);
            aliveAnimals.add(animal);
        }

        MapVisualizer visualizer = new MapVisualizer(worldMap);
        for (int i = 0; i < 10; i++) {
            //1. Usunięcie martwych zwierzaków z mapy.
            removeDeadAnimals();

            //2. Skręt i przemieszczenie każdego zwierzaka.
            moveAnimals();

            //3. Konsumpcja roślin, na których pola weszły zwierzaki.
            consumption();

            //4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            copulateAnimals();

            //5. Wzrastanie nowych roślin na wybranych polach mapy.
            growNewPlants();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //3. Konsumpcja roślin, na których pola weszły zwierzaki.
        /* Tutaj jest pytanie, czy kilka zwierzakow moze zjesc te rosline?
        Jesli nie, mozna to zjadanie dorzucic zaraz po move
        Jesli tak, trzeba przechowywac gdzies te planty do usuniecia i usunac je na koncu
         */
        //4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
        // Tutaj trzeba przejść po mapie i zobaczyć, czy na jakimś polu jest >=2 zwierzaków i odpalić komparator
        // Stworzyć też jakąś logikę tworzenia dzieci i dziedziczenia genów

        //5. Wzrastanie nowych roślin na wybranych polach mapy.
        //Tak samo gdzies trzeba usypiać wątek tam wyżej, żeby symulacja jednak trwała chwile
    }
    
    void removeDeadAnimals() {
        Set<Animal> deadAnimals = new HashSet<>();

        for (Animal animal : aliveAnimals) {
            if (animal.getEnergy() <= 0) {
                deadAnimals.add(animal);
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

    void copulateAnimals() {
        worldMap.copulateAnimals();
    }

    void growNewPlants() {
        worldMap.growNewPlants(plantsPerDay);
    }
}
