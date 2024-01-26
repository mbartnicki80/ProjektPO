package agh.ics.oop.model.genome;

import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

public abstract class AbstractGenome implements Genome {
    private final int[] genome;
    private int currentGenomeIndex;
    protected final Random random = new Random();  // czemu nie private static?

    protected AbstractGenome(int genomeLength) {
        this.genome = generateGenome(genomeLength);
        this.currentGenomeIndex = random.nextInt(genomeLength);
    }

    protected AbstractGenome(int genomeLength, int minimalMutations, int maximalMutations,
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
                                   int minimalMutations, int maximalMutations, double dominantEnergyProportion) { // skoro to jest metoda niestatyczna, to po pierwsze dostaje 3 genotypy: this, dominantAnimalGenome i reproductionPartnerGenome, a po drugie, po co dostaje długość genomu i limit mutacji?

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

        return mutateGenome(newbornGenome, mutationsCount);  // zaburzenie poziomu abstrakcji
    }

    @Override
    public boolean equals(Object other) {

        if (this == other)
            return true;

        if (!(other instanceof AbstractGenome that))
            return false;

        if (this.genome.length != that.genome.length)
            return false;

        for (int i = 0; i < this.genome.length; i++)
            if (this.genome[i] != that.genome[i]) // https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#equals-int:A-int:A-
                return false;

        return true;
    }

    @Override
    public String toString() {
        StringBuilder genomeString = new StringBuilder();
        for (int gene : this.genome)
            genomeString.append(gene);
        return genomeString.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object) this.genome);
    }
}
