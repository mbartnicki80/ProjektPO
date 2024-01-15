package agh.ics.oop.model;

import java.util.Random;
import java.util.stream.IntStream;

public class Genome {

    private final int[] genome;
    private int currentGenomeIndex;
    private final Random random = new Random();

    public Genome(int genomeLength) {
        this.genome = generateGenome(genomeLength);
        this.currentGenomeIndex = random.nextInt(genomeLength);
    }

    public Genome(int genomeLength, int minimalMutations, int maximalMutations,
                  Genome dominantAnimalGenome, Genome reproductionPartnerGenome, double dominantEnergyProportion) {
        this.genome = reproducedGenome(genomeLength, dominantAnimalGenome, reproductionPartnerGenome,
                                        minimalMutations, maximalMutations, dominantEnergyProportion);
        this.currentGenomeIndex = random.nextInt(genomeLength);
    }

    private int[] generateGenome(int genomeLength) {
        return IntStream.generate(() -> random.nextInt(8))
                .limit(genomeLength)
                .toArray();
    }

    public int useCurrentGene() {
        int move = getCurrentGenome();
        incrementCurrentGenomeIndex();
        return move;
    }

    public int getCurrentGenome() {
         return genome[currentGenomeIndex];
    }

    private void incrementCurrentGenomeIndex() {
        this.currentGenomeIndex = (this.currentGenomeIndex + 1) % this.genome.length;
    }

    public int getGenomeAtIndex(int index) {
        return genome[index];
    }

    private int[] reproducedGenome(int genomeLength, Genome dominantAnimalGenome, Genome reproductionPartnerGenome,
                                  int minimalMutations, int maximalMutations, double dominantEnergyProportion) {

        int[] newbornGenome = new int[genomeLength];

        boolean leftSideFromDominant = random.nextDouble() < 0.5;

        if (leftSideFromDominant) {
            int leftGenesCount = (int) (genomeLength * dominantEnergyProportion);
            for (int i = 0; i < leftGenesCount; i++)
                newbornGenome[i] = dominantAnimalGenome.getGenomeAtIndex(i);
            for (int i = leftGenesCount; i < genomeLength; i++)
                newbornGenome[i] = reproductionPartnerGenome.getGenomeAtIndex(i);
        }
        else {
            int leftGenesCount = genomeLength - (int) (genomeLength * dominantEnergyProportion);
            for (int i = 0; i < leftGenesCount; i++)
                newbornGenome[i] = reproductionPartnerGenome.getGenomeAtIndex(i);
            for (int i = leftGenesCount; i < genomeLength; i++)
                newbornGenome[i] = dominantAnimalGenome.getGenomeAtIndex(i);
        }

        int mutationsCount = random.nextInt(maximalMutations - minimalMutations + 1) + minimalMutations;
        for (int i = 0; i < mutationsCount; i++) {
            int mutationIndex = random.nextInt(genomeLength);
            newbornGenome[mutationIndex] = random.nextInt();
        }

        return newbornGenome;
    }
}
