package agh.ics.oop.model;

import agh.ics.oop.Listener;

@FunctionalInterface
public interface MapChangeListener extends Listener {
    void mapChanged(WorldMap worldMap, String message);
}
