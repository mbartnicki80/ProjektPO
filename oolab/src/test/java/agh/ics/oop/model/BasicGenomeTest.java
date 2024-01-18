package agh.ics.oop.model;

import agh.ics.oop.model.genome.BasicGenome;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicGenomeTest {

    @Test
    void mutateGenome() {


        int[] genome = {1, 2, 3, 4, 5};

        int[] mutated = new BasicGenome(5).mutateGenome(genome, 10);

        int mutations = 0;
        for (int i = 0; i < genome.length; i++) {
            if (genome[i] != mutated[i]) {
                mutations++;
            }
        }

        assertEquals(0, mutations);
    }
}