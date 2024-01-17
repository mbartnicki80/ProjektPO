package agh.ics.oop.model;

public interface Genome {
    int[] mutateGenome(int[] newbornGenome, int mutationsCount);

    int useCurrentGene();

    int getGenomeAtIndex(int i);
}
