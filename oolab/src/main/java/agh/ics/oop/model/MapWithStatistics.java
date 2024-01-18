package agh.ics.oop.model;

import java.util.List;

public interface MapWithStatistics extends MoveValidator {
    int getNumberOfAnimals();
    int getNumberOfPlants();
    int getFreeSpace();
    Genome getDominantGenome();
    int getAverageEnergy();
    int getAverageLifeLengthOfDeadAnimals();
    int getAverageChildrenCount();
    int getDay();

    List<Vector2d> getPreferredPositions();

}
