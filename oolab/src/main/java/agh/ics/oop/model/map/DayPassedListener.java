package agh.ics.oop.model.map;

import agh.ics.oop.model.observers.Listener;

public interface DayPassedListener extends Listener {

    void dayUpdate(MapWithStatistics worldMap);

}
