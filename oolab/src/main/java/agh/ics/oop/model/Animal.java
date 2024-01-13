package agh.ics.oop.model;
import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    private int energy;
    private int childrenCounter; //zrobic jako liste potomkow
    private final int dayOfBirth;
    private static final MapDirection[] directions = MapDirection.values();

    public Animal(Vector2d position, MapDirection orientation, int energy, int dayOfBirth, int genomeLength) {
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
        this.childrenCounter = 0; //jako pusta lista
        this.dayOfBirth = dayOfBirth;
        this.genome = new Genome(genomeLength);
    }

    public Animal(Vector2d position, int energy, int dayOfBirth, Genome genome) {
        this.position = position;
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];
        this.energy = energy;
        this.childrenCounter = 0;
        this.dayOfBirth = dayOfBirth;
        this.genome = genome;
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

    public boolean isDead() {return energy <= 0;}

    public int getEnergy() {
        return energy;
    }

    public int getChildrenCount() {
        return childrenCounter;
    }

    public int getDayOfBirth() {
        return dayOfBirth;
    }

    public Animal reproduce(Animal reproductionPartner, int day, int genomeLength,
                            int minimalMutations, int maximalMutations, int newbornEnergy) {

        Genome newbornGenome = new Genome(genomeLength, minimalMutations, maximalMutations,
                                this.getGenome(), reproductionPartner.getGenome(),
                (double) this.getEnergy() / (this.getEnergy() + reproductionPartner.getEnergy()));

        Animal newbornAnimal = new Animal(reproductionPartner.getPosition(), newbornEnergy, day, newbornGenome);

        this.addChildren(); reproductionPartner.addChildren();
        return newbornAnimal;
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

    public void move(Vector2d newPosition, MapDirection newOrientation, MoveValidator moveValidator) {

        if (moveValidator.canMoveTo(newPosition)) {
            this.position = newPosition;
            this.orientation = newOrientation;
        }

    }

    public void useEnergy (int energyToUse) {
        energy -= energyToUse;
    }

    public void addChildren() { //tutaj ewentualnie przerobic, zeby przyjmowalo animala i dodawalo mu do listy
        childrenCounter += 1;
    }

    public Genome getGenome() {
        // #TODO
        return genome;
    }
}
