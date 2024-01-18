package agh.ics.oop.model.genome;

public class BasicGenome extends AbstractGenome {
    public BasicGenome(int genomeLength) {
        super(genomeLength);
    }

    public BasicGenome(int genomeLength, int minimalMutations, int maximalMutations,
                       Genome dominantAnimalGenome, Genome reproductionPartnerGenome, double dominantEnergyProportion) {
        super(genomeLength, minimalMutations, maximalMutations,
                dominantAnimalGenome, reproductionPartnerGenome, dominantEnergyProportion);
    }

    @Override
    public int[] mutateGenome(int[] newbornGenome, int mutationsCount) {
        return newbornGenome;
    }
}
