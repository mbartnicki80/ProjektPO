package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
        SimulationPresenter presenter = loader.getController();

        List<String> rawParams = getParameters().getRaw();
        List<MoveDirection> directions;
        directions = OptionsParser.convertStringToMoveDirection(rawParams);
        RandomSimulationGenerator generatedSimulations = new RandomSimulationGenerator(directions, 1);
        List<Simulation> simulations = new ArrayList<>();
        for (Simulation generatedSimulation : generatedSimulations)
            simulations.add(generatedSimulation);
        SimulationEngine multipleSimulations = new SimulationEngine(simulations);
        multipleSimulations.runAsyncInThreadPool();
        multipleSimulations.awaitSimulationsEnd();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
