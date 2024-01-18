package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.PositionOutOfBoundsException;

import java.util.*;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

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
    void move(Animal animal) throws PositionOutOfBoundsException;

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

    void registerObserver(MapChangeListener observer);

    void unregisterObserver(MapChangeListener observer);

    void placePlants(int numOfPlants);

    void growNewPlants(int plantsPerDay);
}
