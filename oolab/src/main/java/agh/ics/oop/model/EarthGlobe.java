package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EarthGlobe extends AbstractWorldMap {

    public EarthGlobe(int mapWidth, int mapHeight, int numOfPlants) {
        super(mapWidth, mapHeight);
        randomlyPlacePlants(mapWidth, mapHeight, numOfPlants);
    }

    private void randomlyPlacePlants(int width, int height, int plantCount) {
        ForestedEquators plantPositions = new ForestedEquators(width, height, plantCount);
        for(Vector2d plantPosition : plantPositions) {
            plants.put(plantPosition, new Plant(plantPosition));
        }
    }

    @Override
    public void growNewPlants(int n) {
        List<Vector2d> freePositions = new ArrayList<>(animals.keySet()
                .stream()
                .filter(position -> !plants.containsKey(position))
                .toList());

        Collections.shuffle(freePositions);

        List<Vector2d> chosenPositions = freePositions.subList(0, Math.min(n, freePositions.size()));

        for (Vector2d position : chosenPositions) {
            Plant plant = new Plant(position);
            plants.put(position, plant);
            mapChanged("New plant " + plant + " has grown at " + plant.position());
        }
    }
}
