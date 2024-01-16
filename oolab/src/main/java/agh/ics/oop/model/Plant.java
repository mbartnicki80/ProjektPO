package agh.ics.oop.model;

import java.util.Objects;

public class Plant implements WorldElement {
    private final Vector2d position;
    private final int plantEnergy;

    public Plant(Vector2d position, int plantEnergy) {
        this.position = position;
        this.plantEnergy = plantEnergy;
    }

    public String toString() {
        return "*";
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public Vector2d position() {
        return position;
    }

    public int getEnergy() {
        return plantEnergy;
    }

    @Override
    public String getImageName() {
        return "src/main/java/agh/ics/oop/resources/plant.png";
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
