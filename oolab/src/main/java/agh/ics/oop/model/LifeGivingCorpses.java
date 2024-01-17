package agh.ics.oop.model;

public class LifeGivingCorpses extends AbstractWorldMap {

    public LifeGivingCorpses(int mapWidth, int mapHeight, int numOfPlants, int plantEnergy) {
        super(mapWidth, mapHeight, numOfPlants, plantEnergy);
    }

    public void placePlants(int mapWidth, int mapHeight, int numOfPlants) {

    }

    public void growNewPlants(int plantsPerDay) {
        //masz tutaj dostep do deadAnimalow, sa zadeklarowane w abstract
        //dodaje je w momencie, gdy uzywamy remove, czyli wtedy, kiedy usuwamy zwierzaki, bo sa martwe
        //powinno dzialac
    }

}
