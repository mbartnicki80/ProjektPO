package agh.ics.oop.model;

import java.util.Random;

public class Genome {

    private final int[] genome;
     public Genome(int genomeLength) {
        this.genome = generateGenome(genomeLength);
    }

    private int[] generateGenome(int genomeLength) {
        Random random = new Random();
        int[] random_genome = new int[genomeLength];
        for (int i=0; i<genomeLength; i++)
            random_genome[i] = random.nextInt(8);
        return random_genome;
    }

    public int getCurrentGenome(int i) {  //nazwa
         return genome[i];
    }
}
