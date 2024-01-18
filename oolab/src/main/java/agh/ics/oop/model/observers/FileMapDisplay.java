package agh.ics.oop.model.observers;

import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.annotations.Observer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Observer
public class FileMapDisplay implements MapChangeListener {

    private int changeCounter;

    public FileMapDisplay() {
        this.changeCounter = 0;
    }

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        String logFileName = "map_" + worldMap.getID() + ".log";

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            writer.println("Change number: " + changeCounter++);
            writer.println(message);
            writer.println(worldMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
