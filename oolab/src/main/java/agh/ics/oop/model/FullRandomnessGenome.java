package agh.ics.oop.model;

public class FullRandomnessGenome extends AbstractGenome {
    public FullRandomnessGenome(int genomeLength) {
        super(genomeLength);
    }

    public FullRandomnessGenome(int genomeLength, int minimalMutations, int maximalMutations,
                                FullRandomnessGenome dominantAnimalGenome, FullRandomnessGenome reproductionPartnerGenome, double dominantEnergyProportion) {
        super(genomeLength, minimalMutations, maximalMutations,
        dominantAnimalGenome, reproductionPartnerGenome, dominantEnergyProportion);
    }

    public int[] mutateGenome(int[] newbornGenome, int mutationsCount) {
        for (int i = 0; i < mutationsCount; i++) {
            int mutationIndex = random.nextInt(newbornGenome.length);
            newbornGenome[mutationIndex] = random.nextInt();
        }
        return newbornGenome;
    }
}
