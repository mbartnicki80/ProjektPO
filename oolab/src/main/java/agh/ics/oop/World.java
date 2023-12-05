package agh.ics.oop;
import agh.ics.oop.model.*;
import javafx.application.Application;

import java.util.*;

public class World {

    static void start() {
        System.out.println("System wystartowal");
    }

    static void stop() {
        System.out.println("System zakonczyl dzialanie");
    }

    public static void main(String[] args) {
        try {
            start();
            Application.launch(SimulationApp.class, args);
            List<MoveDirection> directions;
            directions = OptionsParser.convertStringToMoveDirection(args);
            /*ArrayList<Vector2d> positions = new ArrayList<>(Arrays.asList(new Vector2d(2, 2), new Vector2d(3, 3), new Vector2d(3, 4)));
            RectangularMap worldMap1 = new RectangularMap(5, 5, 1);
            GrassField worldMap2 = new GrassField(10, 2);
            ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
            worldMap1.addObserver(consoleMapDisplay);
            worldMap2.addObserver(consoleMapDisplay);
            Simulation simulation1 = new Simulation(positions, directions, worldMap1);
            Simulation simulation2 = new Simulation(positions, directions, worldMap2);
            ArrayList<Simulation> simulations = new ArrayList<>(Arrays.asList(simulation1, simulation2));*/
            RandomSimulationGenerator generatedSimulations = new RandomSimulationGenerator(directions, 1);
            List<Simulation> simulations = new ArrayList<>();
            for (Simulation generatedSimulation : generatedSimulations)
                simulations.add(generatedSimulation);
            SimulationEngine multipleSimulations = new SimulationEngine(simulations);
            //multipleSimulations.runSync();
            //multipleSimulations.runAsync();
            multipleSimulations.runAsyncInThreadPool();
            multipleSimulations.awaitSimulationsEnd();
            stop();
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            stop();
        }
    }
}
