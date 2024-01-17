package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForestedEquator extends AbstractWorldMap {

    public ForestedEquator(int mapWidth, int mapHeight, int numOfPlants, int plantEnergy) {
        super(mapWidth, mapHeight, numOfPlants, plantEnergy);
    }

    @Override
    public void placePlants(int mapWidth, int mapHeight, int numOfPlants) {
        ForestedEquatorPlantPositionsGenerator plantPositions = new ForestedEquatorPlantPositionsGenerator(mapWidth, mapHeight, numOfPlants);
        for (Vector2d plantPosition : plantPositions) {
            plants.put(plantPosition, new Plant(plantPosition, plantEnergy));
        }
    }

    //tylko to do poprawy
    public void growNewPlants(int plantsPerDay) {
        List<Vector2d> freePositions = new ArrayList<>(animals.keySet()
                .stream()
                .filter(position -> !plants.containsKey(position))
                .toList());

        Collections.shuffle(freePositions);

        List<Vector2d> chosenPositions = freePositions.subList(0, Math.min(plantsPerDay, freePositions.size()));

        for (Vector2d position : chosenPositions) {
            Plant plant = new Plant(position, plantEnergy);
            plants.put(position, plant);
            mapChanged("New plant " + plant + " has grown at " + plant.position());
        }
    }

}
