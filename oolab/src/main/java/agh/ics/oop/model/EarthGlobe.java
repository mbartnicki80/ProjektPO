package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;

public class EarthGlobe extends AbstractWorldMap {
    private final Boundary bounds;
    private final Map<Vector2d, WorldElement> plant = new HashMap<>();

    public EarthGlobe(int mapWidth, int mapHeight, int numOfPlants) {
        super();

        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1));

        randomlyPlaceGrass(mapWidth, mapHeight, numOfPlants);
        //generowanie zwierzakow z pozostalymi parametrami
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

    private void randomlyPlaceGrass(int width, int height, int plantCount) {
        ForestedEquators plantPositions = new ForestedEquators(width, height, plantCount);
        for(Vector2d plantPosition : plantPositions) {
            plant.put(plantPosition, new Plant(plantPosition));
        }
    }

    public boolean isOccupiedByPlant(Vector2d position) {
        return plant.containsKey(position);
    }

    public void eatPlantFromPosition(Vector2d position) {
        plant.remove(position);
    }
}
