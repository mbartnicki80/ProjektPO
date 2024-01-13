package agh.ics.oop.model;

public class EarthGlobe extends AbstractWorldMap {

    public EarthGlobe(int mapWidth, int mapHeight, int numOfPlants) {
        super(mapWidth, mapHeight);

        randomlyPlaceGrass(mapWidth, mapHeight, numOfPlants);
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



    private void randomlyPlaceGrass(int width, int height, int plantCount) {
        ForestedEquators plantPositions = new ForestedEquators(width, height, plantCount);
        for(Vector2d plantPosition : plantPositions) {
            plants.put(plantPosition, new Plant(plantPosition));
        }
    }

    public boolean isOccupiedByPlant(Vector2d position) {
        return plants.containsKey(position);
    }

    public void eatPlantFromPosition(Vector2d position) {
        plants.remove(position);
    }
}
