package agh.ics.oop;

import agh.ics.oop.model.MapWithStatistics;
import agh.ics.oop.model.WorldMap;

import java.io.FileWriter;
import java.io.IOException;

public class CSVDataDisplay implements DayPassedListener {

    private FileWriter fileWriter;
    public CSVDataDisplay() {
        try {
            fileWriter = new FileWriter("data.csv");

            fileWriter.write("day,numberOfAnimals,numberOfPlants,freeSpace,dominantGenome,averageEnergy,averageLifeLengthOfDeadAnimals,averageChildrenCount,preferredPositions\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void dayUpdate(MapWithStatistics worldMapWithStats) {
        System.out.println("ADSKJASHDHASKJDHAKSDHASKJDHASKJDHSA");
        /*
        * "Dzien: " + mapStats.getDay() + "\n" +
                "Liczba zywych zwierzat: " + mapStats.getNumberOfAnimals() + "\n" +
                "Liczba roslin: " + mapStats.getNumberOfPlants() + "\n" +
                "Wolne miejsce: " + mapStats.getFreeSpace() + "\n" +
                "Dominujacy genom: " + "\n" + mapStats.getDominantGenome() + "\n" +
                "Srednia energia zwierzakow: " + mapStats.getAverageEnergy() + "\n" +
                "Srednia dlugosc zycia martwych zwierzat: " + mapStats.getAverageLifeLengthOfDeadAnimals() + "\n" +
                "Srednia liczba dzieci zyjacych zwierzakow: " + mapStats.getAverageChildrenCount(); */

        try (FileWriter fileWriter = new FileWriter("data.csv", true)) {
                fileWriter.write(
                        worldMapWithStats.getDay() + "," +
                                worldMapWithStats.getNumberOfAnimals() + "," +
                                worldMapWithStats.getNumberOfPlants() + "," +
                                worldMapWithStats.getFreeSpace() + "," +
                                worldMapWithStats.getDominantGenome() + "," +
                                worldMapWithStats.getAverageEnergy() + "," +
                                worldMapWithStats.getAverageLifeLengthOfDeadAnimals() + "," +
                                worldMapWithStats.getAverageChildrenCount() + "," +
                                worldMapWithStats.getPreferredPositions().size() + "\n"
                );


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
