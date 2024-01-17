package agh.ics.oop.model;

public class ForestedEquator extends AbstractWorldMap {

    public ForestedEquator(int mapWidth, int mapHeight, int numOfPlants, int plantEnergy) {
        super(mapWidth, mapHeight, numOfPlants, plantEnergy);
    }

    @Override
    public void placePlants(int numOfPlants) {
        ForestedEquatorPlantPositionsGenerator plantPositions = new ForestedEquatorPlantPositionsGenerator(super.plants, super.bounds, numOfPlants);
        for (Vector2d plantPosition : plantPositions) {
            plants.put(plantPosition, new Plant(plantPosition, plantEnergy));
        }
    }

    @Override
    public void growNewPlants(int plantsPerDay) {
        ForestedEquatorPlantPositionsGenerator plantPositions = new ForestedEquatorPlantPositionsGenerator(super.plants, super.bounds, plantsPerDay);
        super.putPlants(plantPositions);
    }

}
