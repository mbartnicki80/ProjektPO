package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.PositionOutOfBoundsException;

import java.util.*;
import java.util.stream.Collectors;

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

    public Vector2d add(Vector2d other, Boundary boundary) throws PositionOutOfBoundsException {

        Vector2d potentialPosition = new Vector2d(x + other.x, y + other.y);

        if (potentialPosition.precedes(boundary.lowerLeft()) || potentialPosition.follows(boundary.upperRight()))
            throw new PositionOutOfBoundsException("Position %s is out of bounds".formatted(potentialPosition.toString()));

        return potentialPosition;
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

    public List<Vector2d> getNeighbors(Boundary boundary) {
        int[] xValues = {x - 1, x, x + 1};
        int[] yValues = {y - 1, y, y + 1};

        return Arrays.stream(xValues)
                .boxed()
                .flatMap(xValue -> Arrays.stream(yValues)
                        .mapToObj(yValue -> new Vector2d(xValue, yValue)))
                .filter(vector2d -> boundary.lowerLeft().precedes(vector2d)
                        && boundary.upperRight().follows(vector2d))
                .collect(Collectors.toList());
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
