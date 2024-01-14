package agh.ics.oop.model;

public record Plant(Vector2d position) implements WorldElement {

    public String toString() {
        return "*";
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }
}
