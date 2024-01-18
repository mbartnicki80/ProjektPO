package agh.ics.oop.model;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {


    public void testMove() {
        WorldMap worldMap = new ForestedEquator(10, 10, 10, 10);

        Animal animal = new Animal(new Vector2d(5, 5), 1, 1, 2);

        worldMap.place(animal);

    }

}