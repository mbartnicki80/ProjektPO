package agh.ics.oop.model;

import agh.ics.oop.model.generators.LifeGivingCorpsesPlantPositionsGenerator;
import agh.ics.oop.model.map.Boundary;
import agh.ics.oop.model.map.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LifeGivingCorpsesPlantPositionsGeneratorTest {

    @Test
    void getPreferredPositions() {

        Animal animal = new Animal(new Vector2d(5, 5), 1, 1, 2);


        List<Vector2d> positions = new LifeGivingCorpsesPlantPositionsGenerator(
                new HashMap<>(),
                new HashSet<>(List.of(animal)),
                new Boundary(new Vector2d(0, 0), new Vector2d(10, 10))).getPreferredPositions();

        assertEquals(9, positions.size());

        assertTrue(positions.contains(animal.position()));
    }
}