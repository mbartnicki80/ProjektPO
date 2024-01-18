package agh.ics.oop.model;

import agh.ics.oop.model.map.Vector2d;

public interface WorldElement {
    Vector2d position();
    int getEnergy();
}
