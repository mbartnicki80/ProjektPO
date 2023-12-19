package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RectangularMapTest {

    @Test
    public void placeTest() {
        Animal animal1 = new Animal(new Vector2d(1, 1));
        Animal animal2 = new Animal(new Vector2d(1, 1));
        Animal animal3 = new Animal(new Vector2d(1, 2));
        Animal animal4 = new Animal(new Vector2d(2, 1));
        Animal animal5 = new Animal(new Vector2d(2, 1));
        Animal animal6 = new Animal(new Vector2d(5, 4));
        Animal animal7 = new Animal(new Vector2d(-1, 0));
        RectangularMap worldMap = new RectangularMap(5, 5);
        List<Animal> animals = List.of(animal1, animal2, animal3, animal4, animal5, animal6, animal7);

        for (Animal currAnimal : animals) {
            try {
                worldMap.place(currAnimal);
            } catch (PositionAlreadyOccupiedException e) {
                assertEquals("Position " + currAnimal.getPosition() + " is already occupied", e.getMessage());
            }
        }
    }

    @Test
    public void moveTest() {
        Animal animal1 = new Animal(new Vector2d(0, 0));
        Animal animal2 = new Animal(new Vector2d(2, 4));
        Animal animal3 = new Animal(new Vector2d(4, 4));
        Animal animal4 = new Animal(new Vector2d(2, 0));
        Vector2d expectedAnimal1 = new Vector2d(0, 1);
        Vector2d notExpectedAnimal1 = new Vector2d(-1, 1);
        Vector2d expectedAnimal2 = new Vector2d(2, 4);
        Vector2d notExpectedAnimal2 = new Vector2d(1, 5);
        Vector2d expectedAnimal3 = new Vector2d(3, 3);
        Vector2d notExpectedAnimal3 = new Vector2d(4, 4);
        Vector2d expectedAnimal4 = new Vector2d(1, 1);
        Vector2d notExpectedAnimal4 = new Vector2d(2, -1);
        RectangularMap worldMap = new RectangularMap(5, 5);
        List<Animal> animals = List.of(animal1, animal2, animal3, animal4);

        for (Animal currAnimal : animals) {
            try {
                worldMap.place(currAnimal);
            } catch (PositionAlreadyOccupiedException e) {
                assertEquals("Position " + currAnimal.getPosition() + " is already occupied", e.getMessage());
            }
        }
        worldMap.move(animal1, MoveDirection.LEFT);
        worldMap.move(animal1, MoveDirection.FORWARD);
        worldMap.move(animal1, MoveDirection.RIGHT);
        worldMap.move(animal1, MoveDirection.FORWARD);

        worldMap.move(animal2, MoveDirection.FORWARD);
        worldMap.move(animal2, MoveDirection.LEFT);
        worldMap.move(animal2, MoveDirection.FORWARD);

        worldMap.move(animal3, MoveDirection.RIGHT);
        worldMap.move(animal3, MoveDirection.FORWARD);
        worldMap.move(animal3, MoveDirection.BACKWARD);

        worldMap.move(animal4, MoveDirection.BACKWARD);
        worldMap.move(animal4, MoveDirection.LEFT);
        worldMap.move(animal4, MoveDirection.FORWARD);

        assertEquals(expectedAnimal1, animal1.getPosition());
        assertNotEquals(notExpectedAnimal1, animal1.getPosition());
        assertEquals(expectedAnimal2, animal2.getPosition());
        assertNotEquals(notExpectedAnimal2, animal2.getPosition());
        assertEquals(expectedAnimal3, animal3.getPosition());
        assertNotEquals(notExpectedAnimal3, animal3.getPosition());
        assertEquals(expectedAnimal4, animal4.getPosition());
        assertNotEquals(notExpectedAnimal4, animal4.getPosition());
    }
    @Test
    public void isOccupiedTest() {
        Animal animal1 = new Animal(new Vector2d(1, 1));
        Animal animal2 = new Animal(new Vector2d(1, 1));
        Animal animal3 = new Animal(new Vector2d(1, 2));
        Animal animal4 = new Animal(new Vector2d(2, 1));
        Animal animal5 = new Animal(new Vector2d(5, 4));
        Animal animal6 = new Animal(new Vector2d(-1, 0));
        RectangularMap worldMap = new RectangularMap(5, 5);
        List<Animal> animals = List.of(animal1, animal2, animal3, animal4, animal5, animal6);

        for (Animal currAnimal : animals) {
            try {
                worldMap.place(currAnimal);
            } catch (PositionAlreadyOccupiedException e) {
                assertEquals("Position " + currAnimal.getPosition() + " is already occupied", e.getMessage());
            }
        }

        assertTrue(worldMap.isOccupied(animal1.getPosition()));
        assertTrue(worldMap.isOccupied(animal2.getPosition()));
        assertTrue(worldMap.isOccupied(animal3.getPosition()));
        assertTrue(worldMap.isOccupied(animal4.getPosition()));
        assertFalse(worldMap.isOccupied(animal5.getPosition()));
        assertFalse(worldMap.isOccupied(animal6.getPosition()));
    }

    @Test
    public void objectAtTest() {
        Animal animal1 = new Animal(new Vector2d(1, 1));
        Animal animal2 = new Animal(new Vector2d(1, 1));
        Animal animal3 = new Animal(new Vector2d(1, 2));
        Animal animal4 = new Animal(new Vector2d(2, 1));
        Animal animal5 = new Animal(new Vector2d(5, 4));
        Animal animal6 = new Animal(new Vector2d(-1, 0));
        RectangularMap worldMap = new RectangularMap(5, 5);
        List<Animal> animals = List.of(animal1, animal2, animal3, animal4, animal5, animal6);

        for (Animal currAnimal : animals) {
            try {
                worldMap.place(currAnimal);
            } catch (PositionAlreadyOccupiedException e) {
                assertEquals("Position " + currAnimal.getPosition() + " is already occupied", e.getMessage());
            }
        }

        assertNotNull(worldMap.objectAt(animal1.getPosition()));
        assertNotNull(worldMap.objectAt(animal2.getPosition()));
        assertNotNull(worldMap.objectAt(animal3.getPosition()));
        assertNotNull(worldMap.objectAt(animal4.getPosition()));
        assertNull(worldMap.objectAt(animal5.getPosition()));
        assertNull(worldMap.objectAt(animal6.getPosition()));
    }
}
