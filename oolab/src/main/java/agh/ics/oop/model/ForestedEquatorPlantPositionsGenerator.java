package agh.ics.oop.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForestedEquatorPlantPositionsGenerator extends AbstractPlantPositionGenerator {

    public ForestedEquatorPlantPositionsGenerator(Map<Vector2d, Plant> plants, Boundary boundary, int grassCount) {
        super(plants, boundary, grassCount);

        super.iterator = generateFinalPositions().iterator();
    }


    protected List<Vector2d> generateFinalPositions() {

        int width = boundary.upperRight().getXValue() + 1;
        int height = boundary.upperRight().getYValue() + 1;

        int equatorHeight = height / 2;

        /* Equator positions */
        List<Vector2d> equatorPositions = IntStream.range(0, width)
                .boxed()
                .map(i -> new Vector2d(i, equatorHeight))
                .filter(position -> !plants.containsKey(position))
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
                .filter(position -> !plants.containsKey(position))
                .collect(Collectors.toList());

        /* Fisher-Yates shuffle */
        Collections.shuffle(equatorPositions, new Random());
        Collections.shuffle(otherPositions, new Random());

        /* Merging with specified probability. */
        return merge(equatorPositions, otherPositions, grassCount);
    }
}
