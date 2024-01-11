package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;

public class EarthGlobe extends AbstractWorldMap {
    private final Boundary bounds;
    private final Map<Vector2d, WorldElement> grass = new HashMap<>();

    public EarthGlobe(int width, int height, int grassCount) {
        super();

        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));

        randomlyPlaceGrass(width, height, grassCount);
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        //trzeba inaczej zrobic logike
        /* w zasadzie to to będzie działać poprawnie
        * bo nie interesują nas boki bo się zapętla,
        * a precedes i follows sprawdzi dół i górę
        * */
        return bounds.lowerLeft().precedes(position) && bounds.upperRight().follows(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        return bounds;
    }

    private void randomlyPlaceGrass(int width, int height, int grassCount) {
        ForestedEquators grassPositions = new ForestedEquators(width, height, grassCount);
        for(Vector2d grassPosition : grassPositions) {
            grass.put(grassPosition, new Grass(grassPosition));
        }
    }
}
