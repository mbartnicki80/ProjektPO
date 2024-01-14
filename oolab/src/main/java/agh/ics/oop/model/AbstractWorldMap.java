package agh.ics.oop.model;
import agh.ics.oop.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID ID;

    protected final Boundary bounds;

    protected final Map<Vector2d, Set<WorldElement>> animals = new HashMap<>();
    protected final Map<Vector2d, WorldElement> plants = new HashMap<>();

    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    private final AnimalComparator animalComparator = new AnimalComparator();

    protected AbstractWorldMap(int mapWidth, int mapHeight) {
        this.ID = UUID.randomUUID();

        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1));

        /* Brucik trochę
        * Można się zastanowić jak to najładniej zastąpić,
        * bo tworzyć zbędnie n^2 obiektów trochę szkoda */

        //myślę że bez znaczenia bo jest spora szansa i tak że każda komórka będzie odwiedzona chociaż raz więc
        //i tak się kiedyś wygeneruje to cos
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

        Vector2d currentPosition = animal.position();
        MapDirection currentOrientation = animal.getOrientation();

        MapDirection newOrientation = currentOrientation.rotate(animal.useCurrentAnimalGene());
        Vector2d newPosition = currentPosition.add(newOrientation.toUnitVector());

        animal.move(newPosition, newOrientation, this);
        animal.useEnergy(1);

        animals.get(currentPosition).remove(animal);


        /* Tu wyrzuca null ptr, dlaczegooo? */
        animals.get(newPosition).add(animal);
        /*Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.util.Set.add(Object)" because the return value of "java.util.Map.get(Object)" is null
            at agh.ics.oop.model.AbstractWorldMap.move(AbstractWorldMap.java:64)
            at agh.ics.oop.Simulation.moveAnimals(Simulation.java:128)
            at agh.ics.oop.Simulation.run(Simulation.java:84)
            at agh.ics.oop.WorldConsole.main(WorldConsole.java:27)
        */


        mapChanged("Animal was moved from " + currentPosition + " " + " to " + newPosition);

    }

    public void place(Animal animal) {
        //wydaje mi sie ze wystarczy
        if (canMoveTo(animal.position()))
            animals.get(animal.position()).add(animal);

        /*
        if (isOccupied(animal.getPosition())) {
            animals.get(animal.getPosition()).add(animal);
        }
        else {
            Set<WorldElement> elements = new HashSet<>();
            elements.add(animal);
            animals.put(animal.getPosition(), elements);
        }*/
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public Optional<WorldElement> objectAt(Vector2d position) {
        return Optional.ofNullable(animals.get(position))
                .flatMap(set -> set.stream().findAny())
                .or(() -> Optional.ofNullable(plants.get(position)));
    }

    public List<WorldElement> getElements() {
        return Stream
                .concat(animals.values().stream().flatMap(Set::stream), plants.values().stream())
                .collect(Collectors.toList());
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

                Animal dominantAnimal = Collections.max(animalsAtPosition, animalComparator);

                dominantAnimal.eatPlant(plantEnergy);
                remove(plants.get(plantPosition));
            }
        }

    }

    @Override
    public ArrayList<Animal> reproduceAnimals(int day, int genomeLength, int minimalMutations, int maximalMutations,
                                             int reproductionReadyEnergy, int usedReproductionEnergy) {
        ArrayList<Animal> newbornAnimals = new ArrayList<>();

        animals.forEach((position, animalsAtPosition) -> {
            if (animalsAtPosition.size() > 1) {
                List<Animal> sortedAnimalsAtPosition = animalsAtPosition
                        .stream()
                        .map(animal -> (Animal) animal)
                        .sorted(animalComparator)
                        .toList();

                Animal dominantAnimal = sortedAnimalsAtPosition.get(0);
                Animal reproductionPartner = sortedAnimalsAtPosition.get(1);
                if (dominantAnimal.getEnergy() > reproductionReadyEnergy && reproductionPartner.getEnergy() > reproductionReadyEnergy) {
                    Animal newbornAnimal = dominantAnimal.reproduce(reproductionPartner, day, genomeLength,
                                                                    minimalMutations, maximalMutations, usedReproductionEnergy * 2);
                    dominantAnimal.useEnergy(usedReproductionEnergy); reproductionPartner.useEnergy(usedReproductionEnergy);
                    newbornAnimals.add(newbornAnimal);
                }
            }
        });
        return newbornAnimals;
    }

    @Override
    public void remove(WorldElement element) {
        if (element instanceof Animal animal) {
            animals.get(element.position()).remove(animal);
            mapChanged("Animal " + animal + " died at " + animal.position());
        }
        else if (element instanceof Plant plant) {
            plants.remove(element.position());
            mapChanged("Plant " + plant + " has been eaten at " + plant.position());
        }
    }

    @Override
    public ArrayList<Animal> getOrderedAnimals() {
        ArrayList<Animal> orderedAnimals = new ArrayList<>();

        animals.forEach((position, animalsAtPosition) -> {
            for (WorldElement animal : animalsAtPosition)
                orderedAnimals.add((Animal) animal);
        });

        orderedAnimals.sort(Comparator.comparing(animal -> animal.getPosition().getXValue())
                .thenComparing(animal -> animal.getPosition().getYValue()));

        return orderedAnimals;
    }

    @Override
    public void growNewPlants(int n) {

    }
}
