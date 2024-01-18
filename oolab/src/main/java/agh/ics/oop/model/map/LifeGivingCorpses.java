package agh.ics.oop.model.map;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.generators.LifeGivingCorpsesPlantPositionsGenerator;
import agh.ics.oop.model.Plant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LifeGivingCorpses extends AbstractWorldMap {

    private final Set<Animal> recentlyDeadAnimals = new HashSet<>();

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
        updateCorpses();
        LifeGivingCorpsesPlantPositionsGenerator plantPositions = new LifeGivingCorpsesPlantPositionsGenerator(super.plants, recentlyDeadAnimals, super.bounds, plantsPerDay);
        super.putPlants(plantPositions);
    }

    public void removeDeadAnimal(Animal animal) {
        animals.get(animal.position()).remove(animal);
        super.deadAnimals.add(animal);
        recentlyDeadAnimals.add(animal);
        animal.setDayOfDeath(day);
        mapChanged("Animal " + animal + " died at " + animal.position());
    }

    public void updateCorpses() {
        recentlyDeadAnimals.removeIf(animal -> day - animal.getDayOfDeath() > 3);
    }

    @Override
    public List<Vector2d> getPreferredPositions() {
        return new LifeGivingCorpsesPlantPositionsGenerator(super.plants, recentlyDeadAnimals, super.bounds)
                .getPreferredPositions();
    }
}
