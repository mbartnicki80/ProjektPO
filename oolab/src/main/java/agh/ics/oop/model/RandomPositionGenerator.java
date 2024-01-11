package agh.ics.oop.model;

import agh.ics.oop.model.Vector2d;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {

    private final Iterator<Vector2d> iterator;

    public RandomPositionGenerator(int width, int height, int grassCount) {

        List<Vector2d> positions = IntStream.range(0, height)
                .boxed()
                .flatMap(i -> IntStream.range(0, width).mapToObj(j -> new Vector2d(j, i)))
                .collect(Collectors.toList());

        /* Fisher-Yates shuffle */
        Collections.shuffle(positions, new Random());

        positions = positions.subList(0, grassCount);

        this.iterator = positions.iterator();
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
}