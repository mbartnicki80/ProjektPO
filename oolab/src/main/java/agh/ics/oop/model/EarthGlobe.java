package agh.ics.oop.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EarthGlobe extends AbstractWorldMap {
    private final Boundary bounds;
    private final Map<Vector2d, WorldElement> grass = new HashMap<>();

    public EarthGlobe(int width, int height) {
        super();
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(width-1, height-1));
        //generowanie trawy, mam RandomPositionGenerator - mozna go wykorzystac do
    }

    @Override
    public boolean canMoveTo(Vector2d position) { //trzeba inaczej zrobic logike
        return bounds.lowerLeft().precedes(position) && bounds.upperRight().follows(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        return bounds;
    }
}