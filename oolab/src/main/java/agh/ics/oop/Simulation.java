package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.AnimalFactory;
import agh.ics.oop.model.map.Boundary;
import agh.ics.oop.model.map.Vector2d;
import agh.ics.oop.model.map.WorldMap;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Simulation implements Runnable {

    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final WorldMap worldMap;
    private final int plantsPerDay;
    private final int reproductionReadyEnergy;
    private final int usedReproductionEnergy;
    private boolean isRunning = true;
    private final int speed;
    private final Lock lock = new ReentrantLock();
    private final Condition pauseCondition = lock.newCondition();


    public Simulation(WorldMap worldMap, int numberOfAnimals, int startAnimalEnergy,
                      int plantsPerDay, int reproductionReadyEnergy, int usedReproductionEnergy,
                      int minimalMutations, int maximalMutations, int genomeLength, boolean fullRandomnessGenome, int speed) {

        this.worldMap = worldMap;
        this.plantsPerDay = plantsPerDay;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.usedReproductionEnergy = usedReproductionEnergy;
        this.speed = speed;
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

    public void run() {

        try {
            while (!aliveAnimals.isEmpty()) {
                lock.lock();
                try {
                    while(!isRunning) {
                        pauseCondition.await();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } finally {
                    lock.unlock();
                }
                removeDeadAnimals();
                moveAnimals();
                consumption();
                reproduceAnimals();
                growNewPlants();
                worldMap.dayUpdate();
                Thread.sleep(speed);
            }
        } catch (InterruptedException ignored) {}
    }

    private void removeDeadAnimals() {
        Iterator<Animal> iterator = aliveAnimals.iterator();

        iterator.forEachRemaining(
                animal -> {
                    if (animal.isDead()) {
                        worldMap.removeDeadAnimal(animal);
                        iterator.remove();
                    }
                }
        );
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

    public boolean getRunningStatus() {
        return this.isRunning;
    }

    public void pauseSimulation() {
        lock.lock();
        isRunning = false;
        lock.unlock();
    }

    public void resumeSimulation() {
        lock.lock();
        try {
            isRunning = true;
            pauseCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
