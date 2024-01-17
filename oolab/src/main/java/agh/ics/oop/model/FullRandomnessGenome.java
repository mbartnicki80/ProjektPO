package agh.ics.oop.model;

public class FullRandomnessGenome extends BasicGenome {

    public FullRandomnessGenome(int genomeLength, int minimalMutations, int maximalMutations,
                                Genome dominantAnimalGenome, Genome reproductionPartnerGenome, double dominantEnergyProportion) {
        super(genomeLength, minimalMutations, maximalMutations,
        dominantAnimalGenome, reproductionPartnerGenome, dominantEnergyProportion);
    }

    @Override
    public int[] mutateGenome(int[] newbornGenome, int mutationsCount) {
        for (int i = 0; i < mutationsCount; i++) {
            int mutationIndex = random.nextInt(newbornGenome.length);
            newbornGenome[mutationIndex] = random.nextInt(8);
        }
        return newbornGenome;
    }
}
