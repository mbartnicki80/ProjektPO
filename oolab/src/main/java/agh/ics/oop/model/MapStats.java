package agh.ics.oop.model;

public interface MapStats {
    int getNumberOfAnimals();
    int getNumberOfPlants();
    int getFreeSpace();
    Genome getDominantGenome();
    int getAverageEnergy();
    int getAverageLifeLengthOfDeadAnimals();
    int getAverageChildrenCount();

    int getDay();
}
