package agh.ics.oop.model;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    private int energy;
    private int childrenCounter; //zrobic jako liste potomkow
    private final int dayOfBirth;

    public Animal(Vector2d position, MapDirection orientation, int energy, int dayOfBirth, int genomeLength) {
        this.position = position;
        this.orientation = orientation; //zrobic randomowa orientacje chyba przy inicjowaniu tutaj
        this.energy = energy;
        this.childrenCounter = 0; //jako pusta lista
        this.dayOfBirth = dayOfBirth;
        this.genome = new Genome(genomeLength);
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int useCurrentAnimalGene() {
        return genome.useCurrentGene();
    }
    public boolean isDead() {return energy<=0;}

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

    //zamienić toString na jakieś ładne obrazki, np strzałki czy coś
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
