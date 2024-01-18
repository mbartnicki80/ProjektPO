package agh.ics.oop.model;

import agh.ics.oop.model.genome.BasicGenome;
import agh.ics.oop.model.genome.Genome;
import agh.ics.oop.model.map.MapDirection;
import agh.ics.oop.model.map.MoveValidator;
import agh.ics.oop.model.map.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Animal implements WorldElement, Comparable<Animal> {

    private MapDirection orientation;
    private Vector2d position;
    private final Genome genome;
    private int energy;
    private final ArrayList<Animal> children = new ArrayList<>();
    private final int dayOfBirth;
    private int dayOfDeath = -1;
    private int plantsEaten = 0;
    private static final MapDirection[] directions = MapDirection.values();
    private final Random random = new Random();

    public Animal(Vector2d position, int energy, int dayOfBirth, int genomeLength) {
        this.position = position;
        this.orientation = directions[random.nextInt(directions.length)];
        this.energy = energy;
        this.dayOfBirth = dayOfBirth;
        this.genome = new BasicGenome(genomeLength);
    }

    public Animal(Vector2d position, int energy, int dayOfBirth, Genome genome) {
        this.position = position;
        this.orientation = directions[random.nextInt(directions.length)];
        this.energy = energy;
        this.dayOfBirth = dayOfBirth;
        this.genome = genome;
    }

    public int useCurrentAnimalGene() {
        return genome.useCurrentGene();
    }

    public void setDayOfDeath(int day) {
        this.dayOfDeath = day;
    }

    public boolean isDead() {
        return energy<=0;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPlantsEatenCount() {
        return plantsEaten;
    }

    public int getChildrenCount() {
        return children.size();
    }

    public int getDayOfBirth() {
        return dayOfBirth;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public int getLifeLength() {
        return dayOfDeath - dayOfBirth;
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

    public boolean move(MoveValidator validator) {
        Vector2d oldPosition = this.position;

        MapDirection newOrientation = this.orientation.rotate(this.useCurrentAnimalGene());
        Vector2d newPosition = this.position.add(newOrientation.toUnitVector());

        if (validator.canMoveTo(newPosition)) {
            this.position = newPosition;
            this.orientation = newOrientation;
        }
        else {
            this.orientation = newOrientation.opposite();
        }

        return !this.position.equals(oldPosition);
    }

    public void useEnergy (int energyToUse) {
        energy -= energyToUse;
    }

    public void addChild(Animal animal) { //tutaj ewentualnie przerobic, zeby przyjmowalo animala i dodawalo mu do listy
        children.add(animal);
    }

    public void setPosition (Vector2d position) {
        this.position = position;
    }

    public Genome getGenome() {
        return genome;
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

    private List<Animal> getDescendants() {
        return children.stream()
                .flatMap(child -> Stream.concat(
                        Stream.of(child), child.getDescendants().stream()))
                .collect(Collectors.toList());
    }

    protected List<Animal> getAliveDescendants() {
        return getDescendants().stream().filter(animal -> animal.getDayOfDeath() == -1).toList();
    }

    public int getDescendantsNumber() {
        return getDescendants().size();
    }

    public int getAliveDescendantsNumber() {
        return getAliveDescendants().size();
    }

}
