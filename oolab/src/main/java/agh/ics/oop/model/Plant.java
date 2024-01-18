package agh.ics.oop.model;

import agh.ics.oop.model.map.Vector2d;

import java.util.Objects;

public class Plant implements WorldElement {
    private final Vector2d position;
    private final int plantEnergy;

    public Plant(Vector2d position, int plantEnergy) {
        this.position = position;
        this.plantEnergy = plantEnergy;
    }

    public String toString() {
        return "#";
    }

    @Override
    public Vector2d position() {
        return position;
    }

    public int getEnergy() {
        return plantEnergy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Plant) obj;
        return Objects.equals(this.position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

}
