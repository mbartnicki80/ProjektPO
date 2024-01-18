package agh.ics.oop.model;

import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.Vector2d;
import agh.ics.oop.model.map.WorldMap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractWorldMapTest {

    @Test
    void getCurrentBounds() {
        WorldMap worldMap = new AbstractWorldMap(5, 5, 5, 5) {
            @Override
            public void placePlants(int numOfPlants) {

            }

            @Override
            public void growNewPlants(int plantsPerDay) {

            }

            @Override
            public List<Vector2d> getPreferredPositions() {
                return null;
            }
        };

        assertEquals(new Vector2d(0, 0), worldMap.getCurrentBounds().lowerLeft());
        assertEquals(new Vector2d(4, 4), worldMap.getCurrentBounds().upperRight());
    }
}