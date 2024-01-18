package agh.ics.oop.model;

import agh.ics.oop.model.map.LifeGivingCorpses;
import agh.ics.oop.model.map.Vector2d;
import agh.ics.oop.model.map.WorldMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LifeGivingCorpsesTest {

    @Test
    void placePlants() {
        WorldMap worldMap = new LifeGivingCorpses(5, 5, 0, 5);
        worldMap.placePlants(5);
        assertEquals(5, worldMap.getNumberOfPlants());
    }

    @Test
    void growNewPlants() {
        WorldMap worldMap = new LifeGivingCorpses(5, 5, 5, 5);
        worldMap.growNewPlants(5);
        assertEquals(10, worldMap.getNumberOfPlants());
    }

    @Test
    void removeDeadAnimal() {
        WorldMap worldMap = new LifeGivingCorpses(5, 5, 5, 5);
        Animal animal = new Animal(new Vector2d(3, 3), 0, 1, 2);
        worldMap.place(animal);
        worldMap.removeDeadAnimal(animal);
        assertEquals(0, worldMap.getNumberOfAnimals());
    }
}