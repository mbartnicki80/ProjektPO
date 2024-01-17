package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getXValue() {
        return x;
    }
    public int getYValue() {
        return y;
    }

    public String toString() {
        return "(%d,%d)".formatted(x, y);
    }

    public boolean precedes(Vector2d other) {
        return (x<=other.x) && (y<=other.y);
    }

    public boolean follows(Vector2d other) {
        return (x>=other.x) && (y>=other.y);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d addModuloX(Vector2d other, int width) {
        return new Vector2d((width + x + other.x) % width, y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x-other.x, y-other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }

    public boolean equals(Object other) {

        if (other == this)
            return true;

        if (!(other instanceof Vector2d that))
            return false;

        return (that.x == x) && (that.y == y);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

}
