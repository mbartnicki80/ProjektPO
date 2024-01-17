package agh.ics.oop.model;

import agh.ics.oop.MapVisualizer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractWorldMap implements WorldMap, MapStats {
    protected final UUID ID;
    protected final Boundary bounds;
    protected final int plantEnergy;
    protected final Map<Vector2d, Set<Animal>> animals = new ConcurrentHashMap<>();
    protected final Set<Animal> deadAnimals = new HashSet<>();
    protected final Map<Vector2d, Plant> plants = new ConcurrentHashMap<>();
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    private final List<List<Vector2d>> positions = new ArrayList<>();
    private AnimalFactory animalFactory;

    public AbstractWorldMap(int mapWidth, int mapHeight, int numOfPlants, int plantEnergy) {
        this.plantEnergy = plantEnergy;
        this.ID = UUID.randomUUID();
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1));

        placePlants(numOfPlants);

        for (int i = 0; i < bounds.upperRight().getXValue(); i++) {
            List<Vector2d> row = new ArrayList<>();
            for (int j = 0; j < bounds.upperRight().getYValue(); j++) {
                row.add(new Vector2d(i, j));
            }
            positions.add(row);
        }

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++){
                Vector2d position = new Vector2d(i, j);
                animals.put(position, new TreeSet<>());
            }
        }
    }

    public void setAnimalFactory(AnimalFactory animalFactory) {
        this.animalFactory = animalFactory;
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
        return bounds.lowerLeft().precedes(position) && bounds.upperRight().follows(position);
    }

    @Override
    public void move(Animal animal) {
        Vector2d previousPosition = animal.position();

        if (animal.move(this)) {
            animals.get(previousPosition).remove(animal);
            animal.useEnergy(1);
            animals.get(animal.position()).add(animal);

            mapChanged("Animal " + animal + " moved from " + previousPosition + " to " + animal.position());
        }
        else {
            mapChanged("Animal " + animal + " cannot execute move.");
        }
    }

    public void place(Animal animal) {
        if (canMoveTo(animal.position()))
            animals.get(animal.position()).add(animal);
    }

    public Optional<WorldElement> objectAt(Vector2d position) {

        return Optional.ofNullable(animals.get(position))
                .orElse(Collections.emptySet())
                .stream()
                .findFirst()
                .map(animal -> (WorldElement) animal)
                .or(() -> Optional.ofNullable((WorldElement) plants.get(position)));
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

        for (Vector2d plantPosition : plantPositions)
            if (!animals.get(plantPosition).isEmpty()) {
                Animal dominantAnimal = Collections.max(animals.get(plantPosition));
                dominantAnimal.eatPlant(plants.get(plantPosition).getEnergy());
                removePlant(plants.get(plantPosition));
            }
    }

    @Override
    public ArrayList<Animal> reproduceAnimals(int day, int reproductionReadyEnergy, int usedReproductionEnergy) {
        ArrayList<Animal> newbornAnimals = new ArrayList<>();

        animals.forEach((position, animalsAtPosition) -> {
            if (animalsAtPosition.size() > 1) {
                List<Animal> dominantAnimals = animalsAtPosition.stream().sorted().toList();
                Animal dominantAnimal = dominantAnimals.get(0);
                Animal reproductionPartner = dominantAnimals.get(1);
                if (dominantAnimal.getEnergy() > reproductionReadyEnergy && reproductionPartner.getEnergy() > reproductionReadyEnergy) {
                    Animal newbornAnimal = animalFactory.create(usedReproductionEnergy * 2, day, dominantAnimal, reproductionPartner);
                    dominantAnimal.useEnergy(usedReproductionEnergy); reproductionPartner.useEnergy(usedReproductionEnergy);
                    newbornAnimals.add(newbornAnimal);
                    this.place(newbornAnimal);
                    mapChanged("New animal has been born at " + newbornAnimal.position());
                }
            }
        });
        return newbornAnimals;
    }

    public void removePlant(Plant plant) {
        plants.remove(plant.position());
        mapChanged("Plant " + plant + " has been eaten at " + plant.position());
    }

    @Override
    public void removeDeadAnimal(Animal animal, int dayOfDeath) {
        animals.get(animal.position()).remove(animal);
        deadAnimals.add(animal);
        animal.setDayOfDeath(dayOfDeath);
        mapChanged("Animal " + animal + " died at " + animal.position());
    }

    public void putPlants(Iterable<Vector2d> plantPositions) {
        for (Vector2d position : plantPositions) {
            Plant plant = new Plant(position, plantEnergy);
            plants.put(position, plant);
            mapChanged("New plant " + plant + " has grown at " + plant.position());
        }
    }

    @Override
    public int getNumberOfAnimals() {
        return animals.values().stream().mapToInt(Set::size).sum();
    }

    @Override
    public int getNumberOfPlants() {
        return plants.size();
    }

    @Override
    public int getFreeSpace() {
        int freeSpaceSum = 0;

        for (int i = 0; i < bounds.upperRight().getXValue(); i++)
            for (int j = 0; j < bounds.upperRight().getYValue(); j++) {
                Vector2d position = positions.get(i).get(j);
                if (objectAt(position).isPresent())
                    freeSpaceSum++;
            }
        return freeSpaceSum;
    }

    @Override
    public Genome getDominantGenome() {
        Map<Genome, Long> frequencyMap = animals.values().stream()
                .flatMap(Set::stream)
                .map(Animal::getGenome)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        if (frequencyMap.isEmpty())
            return null;
        return Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public int getAverageEnergy() {
        return getNumberOfAnimals() != 0
               ? animals.values().stream().mapToInt(animalSet -> animalSet.stream().mapToInt(Animal::getEnergy).sum()).sum() / getNumberOfAnimals()
                : 0;
    }

    @Override
    public int getAverageLifeLengthOfDeadAnimals() {
        return !deadAnimals.isEmpty() ? deadAnimals.stream().mapToInt(Animal::getLifeLength).sum() / deadAnimals.size() : 0;
    }

    @Override
    public int getAverageChildrenCount() {
        return getNumberOfAnimals() != 0
                ? animals.values().stream().mapToInt(animalSet -> animalSet.stream().mapToInt(Animal::getChildrenCount).sum()).sum() / getNumberOfAnimals()
                : 0;
    }
}
