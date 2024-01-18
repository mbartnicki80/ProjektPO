package agh.ics.oop.model;

@FunctionalInterface
public interface MapChangeListener extends Listener {
    void mapChanged(WorldMap worldMap, String message);
}
