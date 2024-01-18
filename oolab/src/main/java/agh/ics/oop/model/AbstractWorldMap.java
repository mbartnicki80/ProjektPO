package agh.ics.oop.model;

import agh.ics.oop.MapVisualizer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractWorldMap implements WorldMap, MapWithStatistics, MoveValidator {
    protected final UUID ID;
    protected final Boundary bounds;
    protected final int plantEnergy;
    protected final Map<Vector2d, Set<Animal>> animals = new ConcurrentHashMap<>();
    protected final Set<Animal> deadAnimals = new HashSet<>();
    protected final Map<Vector2d, Plant> plants = new ConcurrentHashMap<>();
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final List<DayPassedListener> dayObservers = new ArrayList<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    private final List<List<Vector2d>> positions = new ArrayList<>();
    protected int day = 0;
    private AnimalFactory animalFactory;

    public AbstractWorldMap(int mapWidth, int mapHeight, int numOfPlants, int plantEnergy) {
        this.plantEnergy = plantEnergy;
        this.ID = UUID.randomUUID();
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1));

        placePlants(numOfPlants);

        for (int i = 0; i < mapWidth; i++) {
            List<Vector2d> row = new ArrayList<>();
            for (int j = 0; j < mapHeight; j++){
                row.add(new Vector2d(i, j));
                Vector2d position = new Vector2d(i, j);
                animals.put(position, Collections.synchronizedSortedSet(new TreeSet<>()));
            }
            positions.add(row);
        }
    }

    public void setAnimalFactory(AnimalFactory animalFactory) {
        this.animalFactory = animalFactory;
    }

    @Override
    public Boundary getCurrentBounds() {
        return bounds;
    }

    public void registerObserver(Listener observer) {
        if (observer instanceof MapChangeListener)
            observers.add((MapChangeListener) observer);
        else if (observer instanceof DayPassedListener)
            dayObservers.add((DayPassedListener) observer);
        else
            throw new IllegalArgumentException("Observer must implement MapChangeListener or DayPassedListener");

    }

    public void unregisterObserver(Listener observer) {
        if (observer instanceof MapChangeListener)
            observers.remove((MapChangeListener) observer);
        else if (observer instanceof DayPassedListener)
            dayObservers.remove((DayPassedListener) observer);
        else
            throw new IllegalArgumentException("Observer must implement MapChangeListener or DayPassedListener");
    }


    protected void mapChanged(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getXValue() >= -1 && position.getXValue() <= bounds.upperRight().getXValue() + 1 && position.yCoordinateInMap(bounds.upperRight());
    }

    @Override
    public void move(Animal animal) {
        Vector2d previousPosition = animal.position();

        if (animal.move(this)) {
            animals.get(previousPosition).remove(animal);
            if (animal.position().getXValue() == -1)
                animal.setPosition(new Vector2d(bounds.upperRight().getXValue(), animal.position().getYValue()));
            else if (animal.position().getXValue() == bounds.upperRight().getXValue() + 1) {
                animal.setPosition(new Vector2d(0, animal.position().getYValue()));
            }

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
    public ArrayList<Animal> reproduceAnimals(int reproductionReadyEnergy, int usedReproductionEnergy) {
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
    public void removeDeadAnimal(Animal animal) {
        animals.get(animal.position()).remove(animal);
        deadAnimals.add(animal);
        animal.setDayOfDeath(day);
        mapChanged("Animal " + animal + " died at " + animal.position());
    }

    public void putPlants(Iterable<Vector2d> plantPositions) {
        for (Vector2d position : plantPositions) {
            Plant plant = new Plant(position, plantEnergy);
            plants.put(position, plant);
            mapChanged("New plant " + plant + " has grown at " + plant.position());
        }
    }

    public void dayUpdate() {
        day++;
        animals.forEach((position, animalsAtPosition) -> {
            for (Animal animal : animalsAtPosition) {
                animal.useEnergy(1);
            }
        });

        for (DayPassedListener observer : dayObservers) {
            observer.dayUpdate(this);
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
                if (animals.get(position).isEmpty() && !plants.containsKey(position)) {
                    freeSpaceSum++;
                }
            }
        return freeSpaceSum;
    }

    @Override
    public Genome getDominantGenome() {
        Map<Genome, Integer> frequencyMap = new HashMap<>();

        for (int i = 0; i < bounds.upperRight().getXValue(); i++){
            for (int j = 0; j < bounds.upperRight().getYValue(); j++) {
                Vector2d position = positions.get(i).get(j);
                if (animals.get(position).isEmpty())
                    continue;
                for (Animal animal : animals.get(position)) {
                    if (frequencyMap.containsKey(animal.getGenome()))
                        frequencyMap.put(animal.getGenome(), frequencyMap.get(animal.getGenome()) + 1);
                    else
                        frequencyMap.put(animal.getGenome(), 1);
                }
            }
        }
        if (frequencyMap.isEmpty())
            return null;
        return Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public int getAverageEnergy() {
        if (getNumberOfAnimals() == 0)
            return 0;

        int sumOfEnergy = 0;
        for (int i = 0; i < bounds.upperRight().getXValue(); i++) {
            for (int j = 0; j < bounds.upperRight().getYValue(); j++) {
                Vector2d position = positions.get(i).get(j);
                for (Animal animal : animals.get(position)) {
                    sumOfEnergy += animal.getEnergy();
                }
            }
        }

        return sumOfEnergy / getNumberOfAnimals();
    }

    @Override
    public int getAverageLifeLengthOfDeadAnimals() {
        if (deadAnimals.isEmpty())
            return 0;

        int sumOfDaysLived = 0;
        for (Animal deadAnimal : deadAnimals) {
            sumOfDaysLived += deadAnimal.getLifeLength();
        }

        return sumOfDaysLived / deadAnimals.size();
    }

    @Override
    public int getAverageChildrenCount() {
        if (getNumberOfAnimals() == 0)
            return 0;
        if (getNumberOfAnimals() == 0)
            return 0;

        int sumOfChildren = 0;
        for (int i = 0; i < bounds.upperRight().getXValue(); i++) {
            for (int j = 0; j < bounds.upperRight().getYValue(); j++) {
                Vector2d position = positions.get(i).get(j);
                for (Animal animal : animals.get(position)) {
                    sumOfChildren += animal.getChildrenCount();
                }
            }
        }
        return sumOfChildren / getNumberOfAnimals();
    }

    @Override
    public int getDay() {
        return day;
    }
}
