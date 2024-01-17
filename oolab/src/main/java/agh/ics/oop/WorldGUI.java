package agh.ics.oop;

import javafx.application.Application;

public class WorldGUI {

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
            stop();
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            stop();
        }
    }
}
