package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> simulationThreads = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync() {
        for (Simulation simulation : simulations) {
            simulation.run();
        }
    }

    public void runAsync() {
        for (Simulation simulation : simulations)
            simulationThreads.add(new Thread(simulation));

        for (Thread simulationThread : simulationThreads)
            simulationThread.start();
    }

    public void awaitSimulationsEnd() {
        for (Thread simulationThread : simulationThreads)
            try {
                simulationThread.join();

            } catch (InterruptedException e) {
                System.out.println("Watek zostal przerwany");
                return;
            }

        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Watek zostal przerwany");
        }
    }

    public void runAsyncInThreadPool() {
        for (Simulation simulation : simulations)
            executorService.submit(simulation);
    }
}
