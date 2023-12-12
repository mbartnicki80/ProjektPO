package agh.ics.oop.model;

import java.util.*;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, WorldElement> grass = new HashMap<>();
    private Vector2d lowerLeft;
    private Vector2d upperRight;

    public GrassField(int grassQuantity) {
        super();
        generateGrass(grassQuantity);
    }

    private void generateGrass(int grassQuantity) {
        int n = (int) (Math.sqrt(grassQuantity*10)+1);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(n, n, grassQuantity);
        for(Vector2d grassPosition : randomPositionGenerator)
            grass.put(grassPosition, new Grass(grassPosition));
    }

    private boolean isOccupiedByGrass(Vector2d position) {
        return grass.containsKey(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return (super.isOccupied(position) || isOccupiedByGrass(position));
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement worldElement = super.objectAt(position);
        if (worldElement != null)
            return worldElement;
        return grass.getOrDefault(position, null);
    }

    @Override
    public ArrayList<WorldElement> getElements() {
        ArrayList<WorldElement> elements = super.getElements();
        elements.addAll(grass.values());
        return elements;
    }

    private void calculateCorners(Set<Vector2d> positions) {
        for (Vector2d position : positions) {
            lowerLeft = lowerLeft.lowerLeft(position);
            upperRight = upperRight.upperRight(position);
        }
    }

    @Override
    public Boundary getCurrentBounds() {
        Set<Vector2d> grassPositions = grass.keySet();
        Set<Vector2d> animalsPositions = super.animals.keySet();
        lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        calculateCorners(grassPositions);
        calculateCorners(animalsPositions);
        return new Boundary(lowerLeft, upperRight);
    }
}
