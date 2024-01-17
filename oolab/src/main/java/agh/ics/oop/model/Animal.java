package agh.ics.oop.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement, Comparable<Animal> {

    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    private int energy;
    private final ArrayList<Animal> children = new ArrayList<>();
    private final int dayOfBirth;
    private int dayofDeath = -1;
    private int plantsEaten = 0;
    private static final MapDirection[] directions = MapDirection.values();

    public Animal(Vector2d position, MapDirection orientation, int energy, int dayOfBirth, int genomeLength) {
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
        this.dayOfBirth = dayOfBirth;
        this.genome = new FullRandomnessGenome(genomeLength);
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

    public int useCurrentAnimalGene() {
        return genome.useCurrentGene();
    }

    public void setDayOfDeath(int day) {
        this.dayofDeath = day;
    }

    public boolean isDead() {
        return energy<=0;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPlantsEaten() {
        return plantsEaten;
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

    public int getDayofDeath() {
        return dayofDeath;
    }

    public Animal reproduce(Animal reproductionPartner, int day, int genomeLength,
                            int minimalMutations, int maximalMutations, int newbornEnergy, boolean fullRandomnessGenome) {

        Genome newbornGenome;
        if (fullRandomnessGenome) {
            newbornGenome = new FullRandomnessGenome(genomeLength, minimalMutations, maximalMutations,
                                    this.getGenome(), reproductionPartner.getGenome(),
                    (double) this.getEnergy() / (this.getEnergy() + reproductionPartner.getEnergy()));
        } else
            newbornGenome = new LightCorrectionGenome(genomeLength, minimalMutations, maximalMutations,
                    this.getGenome(), reproductionPartner.getGenome(),
                    (double) this.getEnergy() / (this.getEnergy() + reproductionPartner.getEnergy()));

        Animal newbornAnimal = new Animal(reproductionPartner.position(), newbornEnergy, day, newbornGenome);

        this.addChild(newbornAnimal); reproductionPartner.addChild(newbornAnimal);
        return newbornAnimal;
    }

    public void eatPlant(int plantEnergy) {
        this.energy += plantEnergy;
        this.plantsEaten++;
    }

    public String toString() {
        return orientation.toString();
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
        return genome;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    @Override
    public int compareTo(Animal animal) {
        if (animal==this)
            return 0;
        // Porównywanie energii
        int energyComparison = Integer.compare(this.getEnergy(), animal.getEnergy());
        if (energyComparison != 0) {
            return energyComparison;
        }

        // Jeżeli energia jest taka sama, porównujemy wiek
        int birthComparison = Integer.compare(this.getDayOfBirth(), animal.getDayOfBirth());
        if (birthComparison != 0) {
            return -birthComparison;
        }

        // Jeżeli wiek też jest taki sam, porównujemy liczbę dzieci
        int childrenComparison = Integer.compare(this.getChildrenCount(), animal.getChildrenCount());
        if (childrenComparison != 0) {
            return childrenComparison;
        }

        // Jeżeli liczba dzieci też jest taka sama, zwracamy losowy wynik
        return Math.random() < 0.5 ? -1 : 1;
    }
}
