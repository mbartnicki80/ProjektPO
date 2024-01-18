package agh.ics.oop.model.map;

import agh.ics.oop.model.genome.Genome;

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
