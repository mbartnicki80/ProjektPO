package agh.ics.oop.model;

import java.util.Random;

public class Genome {

    private final int[] genome;
    private int currentGenomeIndex;
    private final Random random = new Random();

    public Genome(int genomeLength) {
        this.genome = generateGenome(genomeLength);
        this.currentGenomeIndex = random.nextInt(genomeLength);
    }

    private int[] generateGenome(int genomeLength) {
        int[] random_genome = new int[genomeLength];
        for (int i=0; i<genomeLength; i++)
            random_genome[i] = random.nextInt(8);
        return random_genome;
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

    //dorobic mutacje
}
