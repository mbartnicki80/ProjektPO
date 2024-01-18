package agh.ics.oop.presenter;

import java.beans.JavaBean;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@JavaBean
public class SimulationConfiguration implements Serializable {
    public UUID configurationNumber;
    public String configurationName;
    public int mapHeight;
    public int mapWidth;
    public int numberOfPlants;
    public int plantEnergy;
    public int plantsPerDay;
    public boolean forestedEquator;
    public int numberOfAnimals;
    public int animalEnergy;
    public int reproductionReadyEnergy;
    public int usedReproductionEnergy;
    public int minimalMutations;
    public int maximalMutations;
    public int genomeLength;
    public boolean fullRandomnessGenome;

    public SimulationConfiguration(int mapHeight, int mapWidth, int numberOfPlants, int plantEnergy,
                                   int plantsPerDay, boolean forestedEquator, int numberOfAnimals,
                                   int animalEnergy, int reproductionReadyEnergy, int usedReproductionEnergy,
                                   int minimalMutations, int maximalMutations, int genomeLength, boolean fullRandomnessGenome,
                                   String configurationName) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.numberOfPlants = numberOfPlants;
        this.plantEnergy = plantEnergy;
        this.plantsPerDay = plantsPerDay;
        this.forestedEquator = forestedEquator;
        this.numberOfAnimals = numberOfAnimals;
        this.animalEnergy = animalEnergy;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.usedReproductionEnergy = usedReproductionEnergy;
        this.minimalMutations = minimalMutations;
        this.maximalMutations = maximalMutations;
        this.genomeLength = genomeLength;
        this.fullRandomnessGenome = fullRandomnessGenome;
        this.configurationNumber = UUID.randomUUID();
        this.configurationName = configurationName;
    }

    public SimulationConfiguration() {}
    public String toString() {
        return configurationName;
    }

    public static void encodeToXML(String path, SimulationConfiguration configuration) {
        try (XMLEncoder xmlEncoder = new XMLEncoder(Files.newOutputStream(Path.of(path)))) {
            xmlEncoder.writeObject(configuration);
        } catch (IOException e) {
            System.out.println(new File(".").getAbsolutePath());
            System.out.println(e.getMessage());
        }
    }

    public static SimulationConfiguration decodeFromXML(String path) {
        try (XMLDecoder xmlDecoder = new XMLDecoder(Files.newInputStream(Path.of(path)))) {
            return (SimulationConfiguration) xmlDecoder.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
