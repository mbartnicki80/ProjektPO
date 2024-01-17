package agh.ics.oop.model;

public class LightCorrectionGenome extends AbstractGenome {
    
    public LightCorrectionGenome(int genomeLength, int minimalMutations, int maximalMutations,
                                Genome dominantAnimalGenome, Genome reproductionPartnerGenome, double dominantEnergyProportion) {
        super(genomeLength, minimalMutations, maximalMutations,
                dominantAnimalGenome, reproductionPartnerGenome, dominantEnergyProportion);
    }

    public int[] mutateGenome(int[] newbornGenome, int mutationsCount) {
        for (int i = 0; i < mutationsCount; i++) {
            int mutationIndex = random.nextInt(newbornGenome.length);

            newbornGenome[mutationIndex] = (8 + (newbornGenome[mutationIndex] + (Math.random() < 0.5 ? -1 : 1))) % 8;
        }
        return newbornGenome;
    }
}
