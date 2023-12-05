package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updates = 0;
    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        System.out.println("Mapa nr: " + worldMap.getID());
        System.out.println("Zmiana nr " + ++updates + ": " + message);
        System.out.println(worldMap);
    }
}
