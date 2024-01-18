package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {

    @Test
    void position() {
        Plant plant = new Plant(new Vector2d(5, 5), 1);
        assertEquals(new Vector2d(5, 5), plant.position());
    }

    @Test
    void getEnergy() {
        Plant plant = new Plant(new Vector2d(5, 5), 173);
        assertEquals(173, plant.getEnergy());
    }
}