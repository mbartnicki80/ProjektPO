package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.*;
import java.util.stream.Stream;

public class Simulation implements Runnable {

    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final WorldMap worldMap;
    private final int plantsPerDay;
    private final int reproductionReadyEnergy;
    private final int usedReproductionEnergy;
    private boolean isRunning = true;

    List<DayPassedListener> dayObservers = new ArrayList<>();

    public Simulation(WorldMap worldMap, int numberOfAnimals, int startAnimalEnergy,
                      int plantsPerDay, int reproductionReadyEnergy, int usedReproductionEnergy,
                      int minimalMutations, int maximalMutations, int genomeLength, boolean fullRandomnessGenome) {

        this.worldMap = worldMap;
        this.plantsPerDay = plantsPerDay;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.usedReproductionEnergy = usedReproductionEnergy;
        AnimalFactory animalFactory = new AnimalFactory(genomeLength, minimalMutations, maximalMutations, fullRandomnessGenome);
        worldMap.setAnimalFactory(animalFactory);
        Random random = new Random();
        Boundary boundary = worldMap.getCurrentBounds();

        List<Vector2d> positions = Stream.generate(() ->
                        new Vector2d(
                                random.nextInt(boundary.upperRight().getXValue()),
                                random.nextInt(boundary.upperRight().getYValue())
                        )
                )
                .limit(numberOfAnimals)
                .toList();

        for (Vector2d position : positions) {
            Animal animal = AnimalFactory.create(position, startAnimalEnergy, 0, genomeLength);
            worldMap.place(animal);
            aliveAnimals.add(animal);
        }
    }

    public void registerDayObserver(DayPassedListener observer) {
        dayObservers.add(observer);
    }

    public void unregisterDayObserver(DayPassedListener observer) {
        dayObservers.remove(observer);
    }

    public void run() {

        try {
            while (!aliveAnimals.isEmpty()) {
                if (isRunning) {

                    removeDeadAnimals();
                    Thread.sleep(300);
                    moveAnimals();
                    Thread.sleep(300);
                    consumption();
                    Thread.sleep(300);
                    reproduceAnimals();
                    Thread.sleep(300);
                    growNewPlants();
                    worldMap.dayUpdate();
                    Thread.sleep(300);
                }
                else
                    Thread.sleep(300);
            }
        } catch (InterruptedException ignored) {}
    }

    private void removeDeadAnimals() {
        Iterator<Animal> iterator = aliveAnimals.iterator();

        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.isDead()) {
                worldMap.removeDeadAnimal(animal);
                iterator.remove();
            }
        }
    }

    private void moveAnimals() {
        for (Animal animal : aliveAnimals) {
            worldMap.move(animal);
        }
    }

    private void consumption() {
        worldMap.consumption();
    }

    private void reproduceAnimals() {
        List<Animal> newbornAnimals = worldMap
                .reproduceAnimals(reproductionReadyEnergy, usedReproductionEnergy);
        aliveAnimals.addAll(newbornAnimals);
    }

    private void growNewPlants() {
        worldMap.growNewPlants(plantsPerDay);
    }

    public void changeRunningMode() {
        this.isRunning = !this.isRunning;
    }

    public boolean getRunningStatus() {
        return this.isRunning;
    }
}
