package agh.ics.oop.model;

import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private int[] genome;
    private int energy;
    private int childrenCounter;
    private int dayOfBirth;

    public Animal(Vector2d position, MapDirection orientation, int energy, int dayOfBirth, int genomeLength) {
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
        this.childrenCounter = 0;
        this.dayOfBirth = dayOfBirth;
        genome = new int[genomeLength];
        Random random = new Random();
        for (int i = 0; i<genomeLength; i++)
            genome[i] = random.nextInt(8); //czy dobra deklaracja dla genome
    }

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

    public int getEnergy() {
        return energy;
    }
    public int getChildrenCount() {
        return childrenCounter;
    }
    public int getDayOfBirth() {
        return dayOfBirth;
    }

    public void eatPlant(int plantEnergy) {
        this.energy += plantEnergy;
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
