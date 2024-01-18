package agh.ics.oop.model;

import java.io.FileWriter;
import java.io.IOException;

public class CSVDataDisplay implements DayPassedListener {

    private final String fileName;
    public CSVDataDisplay(String fileName) {
        this.fileName = fileName;
        try (FileWriter fileWriter = new FileWriter(fileName + ".csv", true)){
            fileWriter.write("day,numberOfAnimals,numberOfPlants,freeSpace,dominantGenome,averageEnergy,averageLifeLengthOfDeadAnimals,averageChildrenCount,preferredPositions\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void dayUpdate(MapWithStatistics worldMapWithStats) {
        try (FileWriter fileWriter = new FileWriter(fileName + ".csv", true)) {
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
