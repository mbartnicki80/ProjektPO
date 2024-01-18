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

    public ForestedEquatorPlantPositionsGenerator(Map<Vector2d, Plant> plants, Boundary boundary) {
        super(plants, boundary);
    }


    protected List<Vector2d> generateFinalPositions() {

        int width = boundary.upperRight().getXValue() + 1;
        int height = boundary.upperRight().getYValue() + 1;

        int equatorHeight = height / 2;

        List<Vector2d> equatorPositions = generateEquatorPositions(width, equatorHeight);
        List<Vector2d> otherPositions = generateOtherPositions(height, width, equatorHeight);

        /* Fisher-Yates shuffle */
        Collections.shuffle(equatorPositions, new Random());
        Collections.shuffle(otherPositions, new Random());

        /* Merging with specified probability. */
        return merge(equatorPositions, otherPositions, grassCount);
    }

    private List<Vector2d> generateEquatorPositions(int width, int equatorHeight) {
        /* Equator positions */
        return IntStream.range(0, width)
                .boxed()
                .map(i -> new Vector2d(i, equatorHeight))
                .filter(position -> !plants.containsKey(position))
                .collect(Collectors.toList());
    }

    private List<Vector2d> generateOtherPositions(int height, int width, int equatorHeight) {
        /* Other positions */
        /* If y-coordinate is >= equator y-coordinate,
         * to avoid duplicates on the equator,
         * new Vector has incremented second coord.
         * Therefore, i stream in range height - 1 */
        return IntStream.range(0, height - 1)
                .boxed()
                .flatMap(i -> IntStream.range(0, width).
                        mapToObj(j -> i >= equatorHeight ?
                                new Vector2d(j, i + 1) : new Vector2d(j, i)))
                .filter(position -> !plants.containsKey(position))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vector2d> getPreferredPositions() {
        return generateEquatorPositions(
                boundary.upperRight().getXValue() + 1,
                (boundary.upperRight().getYValue() + 1) / 2
        );
    }
}
