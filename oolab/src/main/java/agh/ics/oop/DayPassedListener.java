package agh.ics.oop;

import agh.ics.oop.model.MapWithStatistics;
import agh.ics.oop.model.WorldMap;

public interface DayPassedListener extends Listener {

    void dayUpdate(MapWithStatistics worldMap);

}
