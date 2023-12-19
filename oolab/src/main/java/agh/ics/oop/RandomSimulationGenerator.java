package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.*;

public class RandomSimulationGenerator implements Iterable<Simulation> {
    private final List<Simulation> simulations = new ArrayList<>();
    private final List<MoveDirection> directions;
    private final int simulationsQuantity;

    public RandomSimulationGenerator(List<MoveDirection> directions, int simulationsQuantity) {
        this.directions = directions;
        this.simulationsQuantity = simulationsQuantity;
        generateSimulations();
    }

    private List<Vector2d> generatePositions(int width, int height, int animalsQuantity) {
        List<Vector2d> allPositions = new ArrayList<>();
        for (int i=0; i<width; i++)
            for (int j=0; j<height; j++)
                allPositions.add(new Vector2d(i, j));

        Collections.shuffle(allPositions);
        return allPositions.subList(0, animalsQuantity);
    }

    private void generateSimulations() {
        Random rand = new Random();
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();

        for (int i=1; i<=simulationsQuantity; i++) {
            int map = rand.nextInt(2);  // 0 - RectangularMap, 1 - GrassField
            int energy = rand.nextInt(10)+5; //energy bedzie chyba ustalone
            if (map==0) {
                int randWidth = rand.nextInt(50)+1;
                int randHeight = rand.nextInt(50)+1;
                int animalsQuantity = rand.nextInt(randHeight+randWidth)+1;
                RectangularMap worldMap = new RectangularMap(randWidth, randHeight);
                worldMap.addObserver(consoleMapDisplay);
                List<Vector2d> positions = generatePositions(randWidth, randHeight, animalsQuantity);
                simulations.add(new Simulation(positions, directions, worldMap, energy));
            }
            else {
                int randomGrass = rand.nextInt(50)+1;
                int animalsQuantity = rand.nextInt(2*randomGrass)+1;
                GrassField worldMap = new GrassField(randomGrass);
                worldMap.addObserver(consoleMapDisplay);
                List<Vector2d> positions = generatePositions((int) (Math.sqrt(randomGrass*10)+1), (int) (Math.sqrt(randomGrass*10)+1), animalsQuantity);
                simulations.add(new Simulation(positions, directions, worldMap, energy));
            }
        }
    }

    @Override
    public Iterator<Simulation> iterator() {
        return simulations.iterator();
    }
}
