package agh.ics.oop.model.observers;

import agh.ics.oop.model.map.WorldMap;

@FunctionalInterface
public interface MapChangeListener extends Listener {
    void mapChanged(WorldMap worldMap, String message);
}
