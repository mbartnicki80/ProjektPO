package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationPresenter {
    @FXML
    private TextField mapHeightTextField;
    @FXML
    private TextField mapWidthTextField;
    @FXML
    private TextField numOfPlantsTextField;
    @FXML
    private TextField plantEnergyTextField;
    @FXML
    private TextField plantsPerDayTextField;
    @FXML
    private TextField numOfAnimalsTextField;
    @FXML
    private TextField animalEnergyTextField;
    @FXML
    private TextField reproductionReadyEnergyTextField;
    @FXML
    private TextField usedReproductionEnergyTextField;
    @FXML
    private TextField minimalMutationsTextField;
    @FXML
    private TextField maximalMutationsTextField;
    @FXML
    private TextField genomeLengthTextField;

    private final ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
    private final FileMapDisplay fileMapDisplay = new FileMapDisplay();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void onSimulationStartClicked() throws IOException, NumberFormatException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation_view.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationViewPresenter presenter = loader.getController();
        Stage stage = new Stage();
        configureStage(stage, viewRoot);

        int mapHeight = Integer.parseInt(mapHeightTextField.getText());
        int mapWidth = Integer.parseInt(mapWidthTextField.getText());
        int numberOfPlants = Integer.parseInt(numOfPlantsTextField.getText());
        int plantEnergy = Integer.parseInt(plantEnergyTextField.getText());
        int plantsPerDay = Integer.parseInt(plantsPerDayTextField.getText());
        //wariant wzrostu roślin !!checkbox

        int numberOfAnimals = Integer.parseInt(numOfAnimalsTextField.getText());
        int animalEnergy = Integer.parseInt(animalEnergyTextField.getText());
        int reproductionReadyEnergy = Integer.parseInt(reproductionReadyEnergyTextField.getText());
        int usedReproductionEnergy = Integer.parseInt(usedReproductionEnergyTextField.getText());
        int minimalMutations = Integer.parseInt(minimalMutationsTextField.getText());
        int maximalMutations = Integer.parseInt(maximalMutationsTextField.getText());
        int genomeLength = Integer.parseInt(genomeLengthTextField.getText());
        //wariant mutacji !!checkbox

        EarthGlobe worldMap = new EarthGlobe(
                mapWidth,
                mapHeight,
                numberOfPlants,
                plantEnergy
        );

        worldMap.registerObserver(presenter);
        worldMap.registerObserver(consoleMapDisplay);
        worldMap.registerObserver(fileMapDisplay);
        presenter.setWorldMap(worldMap);

        worldMap.registerObserver((map, message) -> System.out.println(LocalDateTime.now() + " " + message));

        Simulation simulation = new Simulation(
                worldMap,
                numberOfAnimals,
                animalEnergy,
                plantsPerDay,
                reproductionReadyEnergy,
                usedReproductionEnergy,
                minimalMutations,
                maximalMutations,
                genomeLength
        );

        executorService.submit(simulation);
        stage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
