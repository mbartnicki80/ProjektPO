package agh.ics.oop.model;

import agh.ics.oop.model.map.ForestedEquator;
import agh.ics.oop.model.map.Vector2d;
import agh.ics.oop.model.map.WorldMap;

class AnimalTest {


    public void testMove() {
        WorldMap worldMap = new ForestedEquator(10, 10, 10, 10);

        Animal animal = new Animal(new Vector2d(5, 5), 1, 1, 2);

        worldMap.place(animal);

    }

}