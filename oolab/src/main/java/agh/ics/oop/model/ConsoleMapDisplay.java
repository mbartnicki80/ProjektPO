package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {

    private int changeCounter;

    public ConsoleMapDisplay() {
        this.changeCounter = 0;
    }

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {

        String view = "####################################\n" +
                worldMap.getID() + "\n" +
                message + "\n" +
                worldMap +
                "Change counter: " + ++this.changeCounter + "\n\n\n";

        System.out.println(view);
    }
}
