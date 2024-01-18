package agh.ics.oop.model;
import agh.ics.oop.model.map.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Vector2dTest {
    @Test
    public void equalsTest() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(1, 2);
        Vector2d v3  = new Vector2d(2, 1);
        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
    }
    @Test
    public void toStringTest() {
        Vector2d v1 = new Vector2d(1, 1);
        String s1 = "(1, 1)";
        Vector2d v2 = new Vector2d(0, 0);

        assertNotEquals(s1, v1.toString());
        assertEquals("(0,0)" , v2.toString());
    }
    @Test
    public void precedesTest() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3  = new Vector2d(2, 1);
        assertTrue(v1.precedes(v1));
        assertTrue(v1.precedes(v2));
        assertFalse(v2.precedes(v3));
        assertFalse(v1.precedes(v3));
    }
    @Test
    public void followsTest() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(0,1 );
        Vector2d v3 = new Vector2d(2, 0);
        Vector2d v4 = new Vector2d(1, 1);
        assertTrue(v1.follows(v1));
        assertTrue(v1.follows(v2));
        assertFalse(v1.follows(v3));
        assertTrue(v1.follows(v4));
    }
    @Test
    public void upperRightTest() {
        Vector2d v1 = new Vector2d(1, 10);
        Vector2d v2 = new Vector2d(3, 25);
        Vector2d UpperRight =  new Vector2d(3, 25);
        Vector2d v3 = new Vector2d(10, 30);
        Vector2d v4 = new Vector2d(-9, 200);
        Vector2d UpperRight2 = new Vector2d(10, 200);

        assertEquals(UpperRight, v1.upperRight(v2));
        assertEquals(UpperRight, v2.upperRight(v1));
        assertEquals(UpperRight2, v4.upperRight(v3));
    }
    @Test
    public void lowerLeftTest() {
        Vector2d v1 = new Vector2d(2, 5);
        Vector2d v2 = new Vector2d(10, -8);
        Vector2d LowerLeft =  new Vector2d(2, -8);
        Vector2d v3 = new Vector2d(1, 5);
        Vector2d v4 = new Vector2d(9, 9);
        Vector2d LowerLeft2 = new Vector2d(1, 5);
        assertEquals(LowerLeft, v1.lowerLeft(v2));
        assertEquals(LowerLeft2, v3.lowerLeft(v4));
        assertEquals(LowerLeft, v2.lowerLeft(v1));
    }
    @Test
    public void addTest() {
        Vector2d v1 = new Vector2d(7, 23);
        Vector2d v2 = new Vector2d(8, 7);
        Vector2d v3 = new Vector2d(15, 30);
        Vector2d v4 = new Vector2d(-29, -1);
        Vector2d v5 = new Vector2d(-1, -5);
        Vector2d v6 = new Vector2d(-30, -6);
        assertEquals(v3, v1.add(v2));
        assertEquals(v3, v2.add(v1));
        assertNotEquals(v5, v6.add(v4));
        assertEquals(v6, v4.add(v5));
    }
    @Test
    public void subtractTest() {
        Vector2d v1 = new Vector2d(2, 100);
        Vector2d v2 = new Vector2d(-9, 50);
        Vector2d v3 = new Vector2d(11, 50);
        Vector2d v4 = new Vector2d(2, -20);
        Vector2d v5 = new Vector2d(2, -9);
        Vector2d v6 = new Vector2d(0, -11);
        assertEquals(v3, v1.subtract(v2));
        assertNotEquals(v3, v2.subtract(v1));
        assertEquals(v6, v4.subtract(v5));
    }
    @Test
    public void oppositeTest() {
        Vector2d v1 = new Vector2d(10,99);
        Vector2d v2 = new Vector2d(-10, -99);
        Vector2d v3 = new Vector2d(0,-1);
        Vector2d v4 = new Vector2d(0,1);
        Vector2d v5 = new Vector2d(1,0 );
        assertEquals(v2, v1.opposite());
        assertEquals(v3, v4.opposite());
        assertNotEquals(v4, v5.opposite());
    }
}