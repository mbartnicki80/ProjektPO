package agh.ics.oop.model;

import agh.ics.oop.model.map.ForestedEquator;
import agh.ics.oop.model.map.WorldMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForestedEquatorTest {

    @Test
    void placePlants() {
        WorldMap worldMap = new ForestedEquator(5, 5, 0, 5);
        worldMap.placePlants(5);
        assertEquals(5, worldMap.getNumberOfPlants());
    }

    @Test
    void growNewPlants() {
        WorldMap worldMap = new ForestedEquator(5, 5, 5, 5);
        worldMap.growNewPlants(5);
        assertEquals(10, worldMap.getNumberOfPlants());
    }
}