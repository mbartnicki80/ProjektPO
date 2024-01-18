package agh.ics.oop.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LifeGivingCorpsesPlantPositionsGenerator extends AbstractPlantPositionGenerator {

    private final Set<Animal> lastEpochDeadAnimals;

    public LifeGivingCorpsesPlantPositionsGenerator(Map<Vector2d, Plant> plants, Set<Animal> lastEpochDeadAnimals, Boundary boundary, int numOfPlants) {

        super(plants, boundary, numOfPlants);

        this.lastEpochDeadAnimals = lastEpochDeadAnimals;

        super.iterator = generateFinalPositions().iterator();
    }

    protected List<Vector2d> generateFinalPositions() {

        int width = boundary.upperRight().getXValue() + 1;
        int height = boundary.upperRight().getYValue() + 1;

        /* Neighbors include position of dead animal itself. */
        /* Positions around recently died animals & not occupied */
        List<Vector2d> positionsAroundDeadAnimals = lastEpochDeadAnimals.stream()
                .map(Animal::position)
                .flatMap(position -> position.getNeighbors(boundary).stream())
                .filter(position -> !plants.containsKey(position))
                .collect(Collectors.toList());

        /* Other positions -> not around animals & not occupied */
        List<Vector2d> otherPositions = IntStream.range(0, height)
                .boxed()
                .flatMap(i -> IntStream.range(0, width)
                        .mapToObj(j -> new Vector2d(j, i)))
                .filter(position -> !plants.containsKey(position))
                .filter(position -> !positionsAroundDeadAnimals.contains(position))
                .collect(Collectors.toList());

        /* Fisher-Yates shuffle */
        Collections.shuffle(positionsAroundDeadAnimals, new Random());
        Collections.shuffle(otherPositions, new Random());

        /* Merging with specified probability. */
        return merge(new ArrayList<>(), otherPositions, super.grassCount);
    }
}
