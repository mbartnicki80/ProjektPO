package agh.ics.oop.model;

import java.util.HashSet;
import java.util.Set;

public class LifeGivingCorpses extends AbstractWorldMap implements Updatable {

    Set<Animal> recentlyDeadAnimals = new HashSet<>();

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
        LifeGivingCorpsesPlantPositionsGenerator plantPositions = new LifeGivingCorpsesPlantPositionsGenerator(super.plants, recentlyDeadAnimals, super.bounds, plantsPerDay);

        super.putPlants(plantPositions);
    }

    public void removeDeadAnimal(Animal animal, int dayOfDeath) {
        animals.get(animal.position()).remove(animal);
        super.deadAnimals.add(animal);
        recentlyDeadAnimals.add(animal);
        animal.setDayOfDeath(dayOfDeath);
        mapChanged("Animal " + animal + " died at " + animal.position());
    }

    public void update(int currentDay) {

        recentlyDeadAnimals.removeIf(animal -> currentDay - animal.getDayOfDeath() > 3);

    }

}
