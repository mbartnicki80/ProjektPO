package agh.ics.oop.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {

    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    private int energy;
    private final ArrayList<Animal> children = new ArrayList<>();
    private final int dayOfBirth;
    private static final MapDirection[] directions = MapDirection.values();

    public Animal(Vector2d position, MapDirection orientation, int energy, int dayOfBirth, int genomeLength) {
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
        this.dayOfBirth = dayOfBirth;
        this.genome = new Genome(genomeLength);
    }

    public Animal(Vector2d position, int energy, int dayOfBirth, Genome genome) {
        this.position = position;
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];
        this.energy = energy;
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

    public List<Animal> getChildrenList() {
        return Collections.unmodifiableList(children);
    }

    public int getChildrenCount() {
        return children.size();
    }

    public int getDayOfBirth() {
        return dayOfBirth;
    }

    public Animal reproduce(Animal reproductionPartner, int day, int genomeLength,
                            int minimalMutations, int maximalMutations, int newbornEnergy) {

        Genome newbornGenome = new Genome(genomeLength, minimalMutations, maximalMutations,
                                this.getGenome(), reproductionPartner.getGenome(),
                (double) this.getEnergy() / (this.getEnergy() + reproductionPartner.getEnergy()));

        Animal newbornAnimal = new Animal(reproductionPartner.position(), newbornEnergy, day, newbornGenome);

        this.addChild(newbornAnimal); reproductionPartner.addChild(newbornAnimal);
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

    @Override
    public Vector2d position() {
        return this.position;
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

    public void addChild(Animal animal) { //tutaj ewentualnie przerobic, zeby przyjmowalo animala i dodawalo mu do listy
        children.add(animal);
    }

    public Genome getGenome() {
        // #TODO
        return genome;
    }


    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

}
