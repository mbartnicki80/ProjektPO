package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.ForestedEquator;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.WorldMap;

import java.util.Date;

public class WorldConsole {
    public static void main(String[] args) {

        WorldMap globe = new ForestedEquator(10, 10, 5, 5);
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();

        globe.registerObserver(consoleMapDisplay);

        globe.registerObserver((MapChangeListener) (worldMap, message) -> System.out.println(new Date() + " " + message));

        Simulation simulation = new Simulation(
                globe,
                2,
                10,
                2,
                5,
                2,
                5,
                10,
                7,
                true,
                300
        );

        simulation.run();

    }
}
