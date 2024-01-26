package agh.ics.oop.model;

import agh.ics.oop.model.genome.FullRandomnessGenome;
import agh.ics.oop.model.genome.Genome;
import agh.ics.oop.model.genome.LightCorrectionGenome;
import agh.ics.oop.model.map.Vector2d;

public class AnimalFactory {
    private final int genomeLength;
    private final int minimalMutations;
    private final int maximalMutations;
    private final boolean fullRandomnessGenome;
    public AnimalFactory(int genomeLength, int minimalMutations, int maximalMutations, boolean fullRandomnessGenome) {
        this.genomeLength = genomeLength;
        this.minimalMutations = minimalMutations;
        this.maximalMutations = maximalMutations;
        this.fullRandomnessGenome = fullRandomnessGenome;
    }

    public static Animal create(Vector2d initialPosition, int initialEnergy, int dayOfBirth, int genomeLength) {  // czemu to jest metoda statyczna?
        return new Animal(initialPosition, initialEnergy, dayOfBirth, genomeLength);
    }

    public Animal create(int initialEnergy, int dayOfBirth, Animal dominantAnimal, Animal reproductionPartner) {

        Genome newbornGenome;
        if (fullRandomnessGenome) {  // może bardziej do tego by się przydała fabryka?
            newbornGenome = new FullRandomnessGenome(genomeLength, minimalMutations, maximalMutations,
                    dominantAnimal.getGenome(), reproductionPartner.getGenome(),
                    (double) dominantAnimal.getEnergy() / (dominantAnimal.getEnergy() + reproductionPartner.getEnergy()));
        } else
            newbornGenome = new LightCorrectionGenome(genomeLength, minimalMutations, maximalMutations,
                    dominantAnimal.getGenome(), reproductionPartner.getGenome(),
                    (double) dominantAnimal.getEnergy() / (dominantAnimal.getEnergy() + reproductionPartner.getEnergy()));

        Animal newbornAnimal = new Animal(reproductionPartner.position(), initialEnergy, dayOfBirth, newbornGenome);

        dominantAnimal.addChild(newbornAnimal); reproductionPartner.addChild(newbornAnimal);
        return newbornAnimal;
    }
}
