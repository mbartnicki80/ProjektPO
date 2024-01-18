package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForestedEquatorPlantPositionsGeneratorTest {

    @Test
    void getPreferredPositions() {

        int mapWidth = 10;
        int mapHeight = 10;
        List<Vector2d> positions =
                new ForestedEquatorPlantPositionsGenerator(
                        new HashMap<>(),
                        new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1)),
                        10
                ).getPreferredPositions();

        assertEquals(mapWidth, positions.size());

        assertAll(() -> {
            for (Vector2d position : positions) {
                assertTrue(position.follows(new Vector2d(0, 0)));
                assertTrue(position.precedes(new Vector2d(mapWidth - 1, mapHeight - 1)));
            }
        });

        assertAll(() -> {
            for (Vector2d position : positions) {
                assertEquals(5, position.getYValue());
            }
        });
    }
}