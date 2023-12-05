package agh.ics.oop.presenter;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.WorldMap;

public class SimulationPresenter {
    private WorldMap worldMap;

    public SimulationPresenter(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }

    public void drawMap() {

    }
}
