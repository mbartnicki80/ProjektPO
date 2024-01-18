package agh.ics.oop.model;
import agh.ics.oop.model.map.MapDirection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void nextMethodTest() {
        assertEquals(MapDirection.EAST, MapDirection.NORTH_EAST.next());
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTH_EAST.next());
        assertEquals(MapDirection.WEST, MapDirection.SOUTH_WEST.next());
        assertEquals(MapDirection.NORTH, MapDirection.NORTH_WEST.next());
        assertEquals(MapDirection.NORTH_EAST, MapDirection.NORTH.next());
        assertEquals(MapDirection.SOUTH_EAST, MapDirection.EAST.next());
        assertEquals(MapDirection.SOUTH_WEST, MapDirection.SOUTH.next());
        assertEquals(MapDirection.NORTH_WEST, MapDirection.WEST.next());
    }
    @Test
    public void previousMethodTest() {
        assertEquals(MapDirection.EAST, MapDirection.SOUTH_EAST.previous());
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTH_WEST.previous());
        assertEquals(MapDirection.WEST, MapDirection.NORTH_WEST.previous());
        assertEquals(MapDirection.NORTH, MapDirection.NORTH_EAST.previous());
        assertEquals(MapDirection.NORTH_EAST, MapDirection.EAST.previous());
        assertEquals(MapDirection.SOUTH_EAST, MapDirection.SOUTH.previous());
        assertEquals(MapDirection.SOUTH_WEST, MapDirection.WEST.previous());
        assertEquals(MapDirection.NORTH_WEST, MapDirection.NORTH.previous());
    }

    @Test
    public void rotateMethodTest() {
        assertEquals(MapDirection.EAST, MapDirection.EAST.rotate(0));
        assertEquals(MapDirection.EAST, MapDirection.NORTH_EAST.rotate(1));
        assertEquals(MapDirection.EAST, MapDirection.NORTH.rotate(2));
        assertEquals(MapDirection.EAST, MapDirection.NORTH_WEST.rotate(3));
        assertEquals(MapDirection.EAST, MapDirection.WEST.rotate(4));
        assertEquals(MapDirection.EAST, MapDirection.SOUTH_WEST.rotate(5));
        assertEquals(MapDirection.EAST, MapDirection.SOUTH.rotate(6));
        assertEquals(MapDirection.EAST, MapDirection.SOUTH_EAST.rotate(7));
    }
}
