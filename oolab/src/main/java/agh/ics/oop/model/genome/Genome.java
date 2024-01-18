package agh.ics.oop.model.genome;

public interface Genome {
    int[] mutateGenome(int[] newbornGenome, int mutationsCount);

    int useCurrentGene();

    int getGenomeAtIndex(int i);

    int getCurrentGenome();
}
