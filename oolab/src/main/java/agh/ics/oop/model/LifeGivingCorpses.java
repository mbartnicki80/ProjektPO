package agh.ics.oop.model;

import java.util.HashSet;
import java.util.Set;

public class LifeGivingCorpses extends AbstractWorldMap {

    Set<Animal> lastEpochDeadAnimals = new HashSet<>();

    public LifeGivingCorpses(int mapWidth, int mapHeight, int numOfPlants, int plantEnergy) {
        super(mapWidth, mapHeight, numOfPlants, plantEnergy);
    }

    @Override
    public void placePlants(int numOfPlants) {
        HashSet<Animal> lastEpochDeadAnimals = new HashSet<>();

        LifeGivingCorpsesPlantPositionsGenerator plantPositions = new LifeGivingCorpsesPlantPositionsGenerator(super.plants, lastEpochDeadAnimals, super.bounds, numOfPlants);
        for (Vector2d plantPosition : plantPositions) {
            plants.put(plantPosition, new Plant(plantPosition, plantEnergy));
        }
    }

    @Override
    public void growNewPlants(int plantsPerDay) {
        //masz tutaj dostep do deadAnimalow, sa zadeklarowane w abstract
        //dodaje je w momencie, gdy uzywamy remove, czyli wtedy, kiedy usuwamy zwierzaki, bo sa martwe
        //powinno dzialac

        /* Lepiej chyba zrobić oddzielny set na te zdechłe animale here,
        * bo w przeciwnym wypadku musimy z każdym wywołaniem growNewPlants iterować po wszystkich
        * zdechłych szukając tych które w danym dniu. Chyba że te zdechłe są tylko po to, to można je
        * wywalić stamtąd albo przenieść tu po prostu */

        LifeGivingCorpsesPlantPositionsGenerator plantPositions = new LifeGivingCorpsesPlantPositionsGenerator(super.plants, lastEpochDeadAnimals, super.bounds, plantsPerDay);
        super.putPlants(plantPositions);
    }


    public void removeDeadAnimal(Animal animal, int dayOfDeath) {
        animals.get(animal.position()).remove(animal);
        super.deadAnimals.add(animal);
        lastEpochDeadAnimals.add(animal);
        animal.setDayOfDeath(dayOfDeath);
        mapChanged("Animal " + animal + " died at " + animal.position());
    }

}
