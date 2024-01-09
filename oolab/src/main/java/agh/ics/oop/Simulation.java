package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {

    private final List<Animal> animals;
    private final List<MoveDirection> moves;
    private final WorldMap worldMap;
    private static final MapDirection[] directions = MapDirection.values();
    private static final Random random = new Random();

    List<Animal> getAnimalsList() {
        return Collections.unmodifiableList(animals);
    }

    public Simulation(List<Vector2d> positions, List<MoveDirection> moves, WorldMap worldMap, int energy, int genomeLength) {
        this.moves = moves;
        this.worldMap = worldMap;
        animals = new ArrayList<>();
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
        //1. Usunięcie martwych zwierzaków z mapy.
        //2. Skręt i przemieszczenie każdego zwierzaka.
        //3. Konsumpcja roślin, na których pola weszły zwierzaki.
        //4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
        //5. Wzrastanie nowych roślin na wybranych polach mapy.
        int animalsSize = animals.size();
        try {
        for (int i=0; i<moves.size(); i++) {
            Animal animal = animals.get(i % animalsSize);
            worldMap.move(animal, moves.get(i));
            Thread.sleep(500);
        }
        } catch (InterruptedException ignored) {}
    }

}
