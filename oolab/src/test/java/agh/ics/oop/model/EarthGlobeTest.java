package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EarthGlobeTest {

    @Test
    void getOrderedAnimals() {

        WorldMap map = new EarthGlobe(10, 10, 5, 5);

        List<Animal> animals = List.of(
                new Animal(
                        new Vector2d(1, 5),
                        MapDirection.NORTH,
                        10,
                        0,
                        7
                ),
                new Animal(
                        new Vector2d(2, 2),
                        MapDirection.NORTH,
                        10,
                        0,
                        7
                ),
                new Animal(
                        new Vector2d(3, 2),
                        MapDirection.NORTH,
                        10,
                        0,
                        7
                ),
                new Animal(
                        new Vector2d(4, 4),
                        MapDirection.NORTH,
                        10,
                        0,
                        7
                ),
                new Animal(
                        new Vector2d(4, 5),
                        MapDirection.NORTH,
                        10,
                        0,
                        7
                )
        );

        for (Animal animal : animals) {
            map.place(animal);
        }

        List<Animal> orderedAnimals = map.getOrderedAnimals();


        assertEquals(animals.size(), orderedAnimals.size());
        for (int i = 0; i < orderedAnimals.size(); i++) {
            assertEquals(orderedAnimals.get(i), animals.get(i));
        }

    }
}