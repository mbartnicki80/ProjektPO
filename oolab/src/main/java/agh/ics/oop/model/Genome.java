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

    //dorobic mutacje
}
