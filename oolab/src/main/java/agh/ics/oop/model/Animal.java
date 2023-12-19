package agh.ics.oop.model;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;


    public Animal(Vector2d position) {
        this.orientation = MapDirection.NORTH;
        this.position = position;
    }

    public Animal() {
        this(new Vector2d(2, 2));
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString() {
        return orientation.toString();
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }


    public void move(MoveDirection direction, MoveValidator moveValidator) {
        switch (direction) {
            case FORWARD, BACKWARD -> {
                Vector2d newPosition = direction == MoveDirection.FORWARD ? position.add(orientation.toUnitVector()) : position.subtract(orientation.toUnitVector());
                if (moveValidator.canMoveTo(newPosition))
                    position = newPosition;
            }
            case RIGHT -> orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
        }
    }
}
