package agh.ics.oop.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LifeGivingCorpsesPlantPositionsGenerator extends AbstractPlantPositionGenerator {

    Set<Animal> lastEpochDeadAnimals;

    public LifeGivingCorpsesPlantPositionsGenerator(Map<Vector2d, Plant> plants, Set<Animal> lastEpochDeadAnimals, Boundary boundary, int numOfPlants) {
        super(plants, boundary, numOfPlants);

        this.lastEpochDeadAnimals = lastEpochDeadAnimals;
    }

    protected List<Vector2d> generateFinalPositions() {

        int width = boundary.upperRight().getXValue() + 1;
        int height = boundary.upperRight().getYValue() + 1;

        List<Vector2d> freeDeadPositions = lastEpochDeadAnimals.stream()
                .map(Animal::position)
                .filter(position -> !plants.containsKey(position))
                .collect(Collectors.toList());

        List<Vector2d> positionsAroundDeadAnimals = lastEpochDeadAnimals.stream()
                .map(Animal::position)
                .flatMap(position -> position.getNeighbors(boundary).stream())
                .filter(position -> !plants.containsKey(position))
                .collect(Collectors.toList());

        positionsAroundDeadAnimals.addAll(freeDeadPositions);

        List<Vector2d> otherPositions = IntStream.range(0, height)
                .boxed()
                .flatMap(i -> IntStream.range(0, width)
                        .mapToObj(j -> new Vector2d(j, i)))
                .filter(position -> !plants.containsKey(position))
                .filter(position -> !positionsAroundDeadAnimals.contains(position))
                .filter(position -> !freeDeadPositions.contains(position))
                .collect(Collectors.toList());


        return merge(positionsAroundDeadAnimals, otherPositions, super.grassCount);
    }
}
