package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.ForestedEquator;
import agh.ics.oop.model.WorldMap;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void run() {

        try {
            WorldMap worldMap = new ForestedEquator(10, 10, 10, 10);

            Simulation simulation = new Simulation(
                    worldMap,
                    2,
                    10,
                    0,
                    20,
                    20,
                    10,
                    10,
                    10,
                    true,
                    10
            );

            simulation.run();


            Class<?> clazz = simulation.getClass();

            Field aliveAnimalsField = clazz.getDeclaredField("aliveAnimals");
            aliveAnimalsField.setAccessible(true);

            Set<Animal> aliveAnimals = (Set<Animal>) aliveAnimalsField.get(simulation);

            assertEquals(0, aliveAnimals.size());

        }
        catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }
}