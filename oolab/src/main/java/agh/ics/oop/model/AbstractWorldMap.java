package agh.ics.oop.model;
import agh.ics.oop.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID ID;

    protected final Boundary bounds;

    protected final Map<Vector2d, Set<WorldElement>> animals = new HashMap<>();
    protected final Map<Vector2d, WorldElement> plants = new HashMap<>();

    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);

    protected AbstractWorldMap(int mapWidth, int mapHeight) {
        this.ID = UUID.randomUUID();

        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1));

        /* Brucik trochę
        * Można się zastanowić jak to najładniej zastąpić,
        * bo tworzyć zbędnie n^2 obiektów trochę szkoda */
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++){
                Vector2d position = new Vector2d(i, j);
                animals.put(position, new HashSet<>());
            }
        }

    }

    @Override
    public Boundary getCurrentBounds() {
        return bounds;
    }

    public void registerObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void unregisterObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    protected void mapChanged(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    public void move(Animal animal) {

        Vector2d currentPosition = animal.getPosition();
        MapDirection currentOrientation = animal.getOrientation();

        MapDirection newOrientation = currentOrientation.rotate(animal.getGenome().useCurrentGene());
        Vector2d newPosition = currentPosition.add(newOrientation.toUnitVector());

        animal.move(newPosition, newOrientation, this);

        animals.get(currentPosition).remove(animal);

        /* Tu wyrzuca null ptr, dlaczegooo? */
        animals.get(newPosition).add(animal);
        /*Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.util.Set.add(Object)" because the return value of "java.util.Map.get(Object)" is null
            at agh.ics.oop.model.AbstractWorldMap.move(AbstractWorldMap.java:64)
            at agh.ics.oop.Simulation.moveAnimals(Simulation.java:128)
            at agh.ics.oop.Simulation.run(Simulation.java:84)
            at agh.ics.oop.WorldConsole.main(WorldConsole.java:27)
        */


        mapChanged("Animal " + animal + " moved from " + currentPosition + " " +
                currentOrientation + " to " + newPosition + " " + newOrientation);

    }

    public void place(Animal animal) {
        if (isOccupied(animal.getPosition())) {
            animals.get(animal.getPosition()).add(animal);
        }
        else {
            Set<WorldElement> elements = new HashSet<>();
            elements.add(animal);
            animals.put(animal.getPosition(), elements);
        }
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public Set<WorldElement> objectAt(Vector2d position) {
        return animals.get(position);
    }

    public ArrayList<WorldElement> getElements() {
        ArrayList<WorldElement> elements = new ArrayList<>();
        for (Set<WorldElement> element : animals.values()) {
            elements.addAll(element);
        }
        return elements;
    }

    @Override
    public String toString() {
        Boundary bounds = getCurrentBounds();
        return mapVisualizer.draw(bounds.lowerLeft(), bounds.upperRight());
    }

    @Override
    public UUID getID() {
        return ID;
    }

    @Override
    public void consumption(int plantEnergy) {

        Collection<Vector2d> plantPositions = plants.keySet();

        for (Vector2d plantPosition : plantPositions) {
            if (animals.containsKey(plantPosition)) {
                List<Animal> animalsAtPosition = animals.get(plantPosition)
                        .stream()
                        .map(animal -> (Animal) animal)
                        .toList();

                Animal dominantAnimal = Collections.max(animalsAtPosition, new AnimalComparator());

                dominantAnimal.eatPlant(plantEnergy);
                remove(plants.get(plantPosition));
            }
        }

    }

    @Override
    public void copulateAnimals() {

    }

    @Override
    public void remove(WorldElement element) {
        if (element instanceof Animal animal) {
            animals.get(element.getPosition()).remove(animal);
            mapChanged("Animal " + animal + " died at " + animal.getPosition());
        }
        else if (element instanceof Plant plant) {
            plants.remove(element.getPosition());
            mapChanged("Plant " + plant + " has been eaten at " + plant.getPosition());
        }
    }

    @Override
    public void growNewPlants(int n) {

    }
}
