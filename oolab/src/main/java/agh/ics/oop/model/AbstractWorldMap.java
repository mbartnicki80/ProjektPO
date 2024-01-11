package agh.ics.oop.model;
import agh.ics.oop.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID ID;
    protected Map<Vector2d, WorldElement> animals = new HashMap<>();
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);

    protected AbstractWorldMap() {
        this.ID = UUID.randomUUID();
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

    public void move(Animal animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        MapDirection oldDirection = animal.getOrientation();
        if (isOccupied(animal.getPosition())) {

            animals.remove(animal.getPosition());
            animal.move(direction, this);
            animals.put(animal.getPosition(), animal);

            Vector2d newPosition = animal.getPosition();
            MapDirection newDirection = animal.getOrientation();

            if (!oldPosition.equals(newPosition))
                mapChanged("Zwierze z pozycji " + oldPosition + " poruszylo sie na pozycje " + newPosition);
            else if (!(oldDirection==newDirection))
                mapChanged("Zwierze z pozycji " + newPosition + " zmienilo orientacje z " + oldDirection + " na " + newDirection);
        }
    }

    public void place(Animal animal) throws PositionAlreadyOccupiedException {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            mapChanged("Zwierze zostalo umieszczone na pozycji: " + animal.getPosition());
        }
        else throw new PositionAlreadyOccupiedException(animal.getPosition());
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public WorldElement objectAt(Vector2d position) {
        return animals.getOrDefault(position, null);
    }

    public ArrayList<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
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
}
