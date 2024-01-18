package agh.ics.oop.model.map;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.AnimalFactory;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.observers.Listener;

import java.util.*;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MapWithStatistics {

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     */
    void place(Animal animal);

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    Optional<WorldElement> objectAt(Vector2d position);

    Boundary getCurrentBounds();

    UUID getID();

    void removeDeadAnimal(Animal animal);

    void consumption();

    void dayUpdate();

    void setAnimalFactory(AnimalFactory animalFactory);

    List<Animal> reproduceAnimals(int genomeLength, int minimalMutations);

    void registerObserver(Listener observer);

    void unregisterObserver(Listener observer);

    void placePlants(int numOfPlants);

    void growNewPlants(int plantsPerDay);
}
