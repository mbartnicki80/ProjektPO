package agh.ics.oop.model;

public class Plant implements WorldElement {
    private final Vector2d position;

    private static int energy;
    public static void setEnergy(int energy) {
        Plant.energy = energy;
    }

    public Plant(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString() {
        return "*";
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }
}
