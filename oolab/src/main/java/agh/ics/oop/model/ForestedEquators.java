package agh.ics.oop.model;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForestedEquators implements Iterable<Vector2d>, Iterator<Vector2d> {

    private final Iterator<Vector2d> iterator;

    public ForestedEquators(int width, int height, int grassCount) {

        int equatorHeight = height / 2;

        /* Equator positions */
        List<Vector2d> equatorPositions = IntStream.range(0, width)
                .boxed()
                .map(i -> new Vector2d(i, equatorHeight))
                .collect(Collectors.toList());


        /* Other positions */
        /* If y-coordinate is >= equator y-coordinate,
        * to avoid duplicates on the equator,
        * new Vector has incremented second coord.
        * Therefore, i stream in range height - 1 */
        List<Vector2d> otherPositions = IntStream.range(0, height - 1)
                .boxed()
                .flatMap(i -> IntStream.range(0, width).
                        mapToObj(j -> i >= equatorHeight ?
                                new Vector2d(j, i + 1) : new Vector2d(j, i)))
                .collect(Collectors.toList());

        /* Fisher-Yates shuffle */
        Collections.shuffle(equatorPositions, new Random());
        Collections.shuffle(otherPositions, new Random());

        List<Vector2d> finalPositions = new ArrayList<>(grassCount);

        int equatorPositionsIterator = 0, otherPositionsIterator = 0;

        /* Merge of positions lists with specified probability */
        Random random = new Random();
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

        this.iterator = finalPositions.iterator();
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
