package agh.ics.oop.model;

public class PositionOutOfBoundsException extends Exception {
    public PositionOutOfBoundsException(Vector2d vector2d) {
        super("Position " + vector2d + " is out of bounds");
    }
}
