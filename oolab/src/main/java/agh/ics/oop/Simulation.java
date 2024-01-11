package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation implements Runnable {

    private final List<Animal> animals; //zamienic liste na cos co przyspieszy usuwanie martwych zwierzakow
    private final WorldMap worldMap;
    private final int plantEnergy;
    private final int plantsPerDay;
    private final int reproductionReadyEnergy;
    private final int usedReproductionEnergy;
    private final int minimalMutations;
    private final int maximalMutations;
    private final int genomeLength;
    private static final MapDirection[] directions = MapDirection.values();

    List<Animal> getAnimalsList() {
        return Collections.unmodifiableList(animals);
    }

    public Simulation(WorldMap worldMap, int numberOfAnimals, int animalEnergy, int plantEnergy,
                      int plantsPerDay, int reproductionReadyEnergy, int usedReproductionEnergy,
                      int minimalMutations, int maximalMutations, int genomeLength) {
        this.worldMap = worldMap;
        this.plantEnergy = plantEnergy;
        this.plantsPerDay = plantsPerDay;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.usedReproductionEnergy = usedReproductionEnergy;
        this.minimalMutations = minimalMutations;
        this.maximalMutations = maximalMutations;
        this.genomeLength = genomeLength;
        animals = new ArrayList<>(); //tutaj generowanie
        for (Vector2d position : positions) {
            int random_orientation = random.nextInt(8);
            Animal newAnimal = new Animal(position, directions[random_orientation], energy, 0, genomeLength);
            try {
                worldMap.place(newAnimal);
                animals.add(newAnimal);
            } catch (PositionAlreadyOccupiedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void run() {
        for (Animal animal : animals) {
            if (animal.isDead()) {
                animals.remove(animal);
                continue;
            }
            MapDirection currentAnimalMove = directions[animal.useCurrentAnimalGene()];
            animal.move(currentAnimalMove, worldMap);
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

        /*try {
        for (int i=0; i<moves.size(); i++) {
            Animal animal = animals.get(i % animalsSize);
            worldMap.move(animal, moves.get(i));
            Thread.sleep(500);
        }
        } catch (InterruptedException ignored) {}*/
        //Tak samo gdzies trzeba usypiać wątek tam wyżej, żeby symulacja jednak trwała chwile
    }

}
