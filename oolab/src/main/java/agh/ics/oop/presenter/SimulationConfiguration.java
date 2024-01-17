package agh.ics.oop.presenter;

public class SimulationConfiguration {
    private static int nextConfigurationNumber = 1;
    private final int configurationNumber;
    private final int mapHeight;
    private final int mapWidth;
    private final int numberOfPlants;
    private final int plantEnergy;
    private final int plantsPerDay;
    private final boolean forestedEquator;
    private final int numberOfAnimals;
    private final int animalEnergy;
    private final int reproductionReadyEnergy;
    private final int usedReproductionEnergy;
    private final int minimalMutations;
    private final int maximalMutations;
    private final int genomeLength;
    private final boolean fullRandomnessGenome;

    public SimulationConfiguration(int mapHeight, int mapWidth, int numberOfPlants, int plantEnergy,
                                   int plantsPerDay, boolean forestedEquator, int numberOfAnimals,
                                   int animalEnergy, int reproductionReadyEnergy, int usedReproductionEnergy,
                                   int minimalMutations, int maximalMutations, int genomeLength, boolean fullRandomnessGenome) {
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
        this.configurationNumber = nextConfigurationNumber++;
    }
    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getPlantsPerDay() {
        return plantsPerDay;
    }

    public boolean isForestedEquator() {
        return forestedEquator;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getAnimalEnergy() {
        return animalEnergy;
    }

    public int getReproductionReadyEnergy() {
        return reproductionReadyEnergy;
    }

    public int getUsedReproductionEnergy() {
        return usedReproductionEnergy;
    }

    public int getMinimalMutations() {
        return minimalMutations;
    }

    public int getMaximalMutations() {
        return maximalMutations;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public boolean isFullRandomnessGenome() {
        return fullRandomnessGenome;
    }

    public String toString() {
        return "Konfiguracja " + this.configurationNumber;
    }
}
