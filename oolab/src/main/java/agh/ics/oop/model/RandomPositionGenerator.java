package agh.ics.oop.model;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private List<Vector2d> positions;
    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        generateGrass();
    }

    private void generateGrass() {
        List<Vector2d> allPositions = new ArrayList<>();
        for (int i=0; i<maxWidth; i++)
            for (int j=0; j<maxHeight; j++)
                allPositions.add(new Vector2d(i, j));

        Collections.shuffle(allPositions);
        positions = allPositions.subList(0, grassCount);
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return positions.iterator();
    }
}
