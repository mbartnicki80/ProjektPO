package agh.ics.oop.model;

import agh.ics.oop.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EarthGlobe implements WorldMap {
    protected final UUID ID;
    protected final Boundary bounds;
    private final int plantEnergy;
    protected final Map<Vector2d, Set<WorldElement>> animals = new HashMap<>();
    protected final Map<Vector2d, WorldElement> plants = new HashMap<>();
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    private final AnimalComparator animalComparator = new AnimalComparator();
    public EarthGlobe(int mapWidth, int mapHeight, int numOfPlants, int plantEnergy) {
        this.plantEnergy = plantEnergy;
        randomlyPlacePlants(mapWidth, mapHeight, numOfPlants);
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

    private void randomlyPlacePlants(int width, int height, int plantCount) {
        ForestedEquators plantPositions = new ForestedEquators(width, height, plantCount);
        for(Vector2d plantPosition : plantPositions) {
            plants.put(plantPosition, new Plant(plantPosition, plantEnergy));
        }
    }

    public void growNewPlants(int plantsPerDay) {
        List<Vector2d> freePositions = new ArrayList<>(animals.keySet()
                .stream()
                .filter(position -> !plants.containsKey(position))
                .toList());

        Collections.shuffle(freePositions);

        List<Vector2d> chosenPositions = freePositions.subList(0, Math.min(plantsPerDay, freePositions.size()));

        for (Vector2d position : chosenPositions) {
            Plant plant = new Plant(position, plantEnergy);
            plants.put(position, plant);
            mapChanged("New plant " + plant + " has grown at " + plant.position());
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

    @Override
    public boolean canMoveTo(Vector2d position) {
        //trzeba inaczej zrobic logike
        /* w zasadzie to to będzie działać poprawnie
         * bo nie interesują nas boki bo się zapętla,
         * a precedes i follows sprawdzi dół i górę
         * */
        return bounds.lowerLeft().precedes(position) && bounds.upperRight().follows(position);
    }

    public void move(Animal animal) {

        Vector2d currentPosition = animal.position();
        MapDirection currentOrientation = animal.getOrientation();

        MapDirection newOrientation = currentOrientation.rotate(animal.useCurrentAnimalGene());
        Vector2d newPosition = currentPosition.add(newOrientation.toUnitVector());


        /* Na razie brzydko ale żeby działalo */
        if (newPosition.getXValue() < 0)
            newPosition = new Vector2d(bounds.lowerLeft().getXValue(), newPosition.getYValue());

        if (newPosition.getXValue() > bounds.upperRight().getXValue())
            newPosition = new Vector2d(bounds.upperRight().getXValue(), newPosition.getYValue());

        if (newPosition.getYValue() < 0) {
            newPosition = new Vector2d(newPosition.getXValue(), bounds.lowerLeft().getYValue());
            animal.setOrientation(animal.getOrientation().rotate(4));
        }

        if (newPosition.getYValue() > bounds.upperRight().getYValue()) {
            newPosition = new Vector2d(newPosition.getXValue(), bounds.upperRight().getYValue());
            animal.setOrientation(animal.getOrientation().rotate(4));
        }

        animals.get(currentPosition).remove(animal);
        animal.move(newPosition, newOrientation, this);
        animal.useEnergy(1);
        animals.get(newPosition).add(animal);



        /* Tu wyrzuca null ptr, dlaczegooo?
         * TODO
         *  jeszcze nie ma pozycji modulo, więc wywala nullptr bo
         *  szuka mapie pozycji spoza mapy
         * */



        /*Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.util.Set.add(Object)" because the return value of "java.util.Map.get(Object)" is null
            at agh.ics.oop.model.AbstractWorldMap.move(AbstractWorldMap.java:64)
            at agh.ics.oop.Simulation.moveAnimals(Simulation.java:128)
            at agh.ics.oop.Simulation.run(Simulation.java:84)
            at agh.ics.oop.WorldConsole.main(WorldConsole.java:27)
        */

        mapChanged("Animal was moved from " + currentPosition + " " + " to " + newPosition);

    }

    public void place(Animal animal) {
        if (canMoveTo(animal.position()))
            animals.get(animal.position()).add(animal);
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

    public void consumption() {

        Collection<Vector2d> plantPositions = plants.keySet();

        for (Vector2d plantPosition : plantPositions) {
            if (animals.containsKey(plantPosition)) {
                List<Animal> animalsAtPosition = animals.get(plantPosition)
                        .stream()
                        .map(animal -> (Animal) animal)
                        .toList();

                Animal dominantAnimal = Collections.max(animalsAtPosition, animalComparator);

                dominantAnimal.eatPlant(plants.get(plantPosition).getEnergy());
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

        /* TODO
         *   test
         * */

        ArrayList<Animal> orderedAnimals = new ArrayList<>();

        animals.forEach((position, animalsAtPosition) -> {
            for (WorldElement animal : animalsAtPosition)
                orderedAnimals.add((Animal) animal);
        });

        orderedAnimals.sort(
                Comparator.comparing(animal -> ((Animal) animal).getPosition().getXValue())
                        .thenComparing(animal -> ((Animal) animal).getPosition().getYValue())
        );

        return orderedAnimals;
    }


}
