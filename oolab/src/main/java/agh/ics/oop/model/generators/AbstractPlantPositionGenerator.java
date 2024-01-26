package agh.ics.oop.model.generators;

import agh.ics.oop.model.map.Boundary;
import agh.ics.oop.model.Plant;
import agh.ics.oop.model.map.Vector2d;

import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractPlantPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {

    protected Iterator<Vector2d> iterator;

    protected Map<Vector2d, Plant> plants;
    protected Boundary boundary;
    protected int grassCount;

    public AbstractPlantPositionGenerator(Map<Vector2d, Plant> plants, Boundary boundary, int grassCount) {
        this.plants = plants;
        this.boundary = boundary;
        this.grassCount = grassCount;
    }


    public AbstractPlantPositionGenerator(Map<Vector2d, Plant> plants, Boundary boundary) {
        this.plants = plants;
        this.boundary = boundary;
    }


    protected abstract List<Vector2d> generateFinalPositions();

    protected static List<Vector2d> merge(List<Vector2d> equatorPositions, List<Vector2d> otherPositions, int grassCount) { // merge co?
        List<Vector2d> finalPositions = new ArrayList<>(grassCount);

        int equatorPositionsIterator = 0, otherPositionsIterator = 0;

        /* Merge of positions lists with specified probability */
        Random random = new Random(); // co wywołanie?
        double equatorProbability = 0.8;
        while (equatorPositionsIterator < equatorPositions.size()
                && otherPositionsIterator < otherPositions.size()
                && finalPositions.size() < grassCount) {

            double randomVal = random.nextDouble(0, 1);

            if (randomVal <= equatorProbability) {
                finalPositions.add(equatorPositions.get(equatorPositionsIterator));
                equatorPositionsIterator++;
            } else {
                finalPositions.add(otherPositions.get(otherPositionsIterator));
                otherPositionsIterator++;
            }
        }

        while (equatorPositionsIterator < equatorPositions.size()
                && finalPositions.size() < grassCount) {

            finalPositions.add(equatorPositions.get(equatorPositionsIterator));
            equatorPositionsIterator++;
        }
        while (otherPositionsIterator < otherPositions.size()
                && finalPositions.size() < grassCount) {

            finalPositions.add(otherPositions.get(otherPositionsIterator));
            otherPositionsIterator++;
        }
        return finalPositions;
    }


    @Override
    public Iterator<Vector2d> iterator() {
        return this.iterator;
    }

    @Override
    public void forEach(Consumer<? super Vector2d> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Vector2d> spliterator() {
        return Iterable.super.spliterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Vector2d next() {
        return iterator.next();
    }


    public abstract List<Vector2d> getPreferredPositions();

}
