package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    @FXML
    private ToggleGroup plantsToggleGroup;
    @FXML
    private ToggleGroup genomeToggleGroup;
    @FXML
    private ComboBox presetConfigurationsComboBox;
    @FXML
    private RadioButton forestedEquatorRadioButton;
    @FXML
    private RadioButton lifeGivingCorpsesRadioButton;
    @FXML
    private RadioButton fullRandomnessRadioButton;
    @FXML
    private RadioButton lightCorrectionRadioButton;
    private final Map<String, SimulationConfiguration> presetConfigurations = new HashMap<>();
    {
        SimulationConfiguration config1 = new SimulationConfiguration(10, 10, 5, 5, 2,
                true, 2, 10, 5, 2, 5,
                10, 7, true);
        SimulationConfiguration config2 = new SimulationConfiguration(8, 9, 3, 2, 1,
                false, 1, 15, 25, 23, 5,
                10, 7, false);

        presetConfigurations.put(config1.toString(), config1);
        presetConfigurations.put(config2.toString(), config2);
    }
    private final ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
    //private final FileMapDisplay fileMapDisplay = new FileMapDisplay();
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

        RadioButton selectedPlantRadioButton = (RadioButton) plantsToggleGroup.getSelectedToggle();
        String plantRadioButtonValue = selectedPlantRadioButton.getText();
        boolean forestedEquator = plantRadioButtonValue.equals("Zalesiony rownik");

        int numberOfAnimals = Integer.parseInt(numOfAnimalsTextField.getText());
        int animalEnergy = Integer.parseInt(animalEnergyTextField.getText());
        int reproductionReadyEnergy = Integer.parseInt(reproductionReadyEnergyTextField.getText());
        int usedReproductionEnergy = Integer.parseInt(usedReproductionEnergyTextField.getText());
        int minimalMutations = Integer.parseInt(minimalMutationsTextField.getText());
        int maximalMutations = Integer.parseInt(maximalMutationsTextField.getText());
        int genomeLength = Integer.parseInt(genomeLengthTextField.getText());

        RadioButton selectedGenomeRadioButton = (RadioButton) genomeToggleGroup.getSelectedToggle();
        String genomeRadioButtonValue = selectedGenomeRadioButton.getText();
        boolean fullRandomnessGenome = genomeRadioButtonValue.equals("Pelna losowosc");

        WorldMap worldMap;
        if (forestedEquator) {
            worldMap = new ForestedEquator(
                    mapWidth,
                    mapHeight,
                    numberOfPlants,
                    plantEnergy
            );
        }
        else {
            worldMap = new LifeGivingCorpses(
                    mapWidth,
                    mapHeight,
                    numberOfPlants,
                    plantEnergy
            );
        }

        worldMap.registerObserver(presenter);
        worldMap.registerObserver(consoleMapDisplay);
        presenter.setWorldMap(worldMap);

        Simulation simulation = new Simulation(
                worldMap,
                numberOfAnimals,
                animalEnergy,
                plantsPerDay,
                reproductionReadyEnergy,
                usedReproductionEnergy,
                minimalMutations,
                maximalMutations,
                genomeLength,
                fullRandomnessGenome
        );

        presenter.setSimulation(simulation);
        executorService.submit(simulation);
        stage.show();
    }

    public void onPresetConfigurationSelected() {
        String selectedConfiguration = presetConfigurationsComboBox.getValue().toString();

        mapHeightTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getMapHeight()));
        mapWidthTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getMapWidth()));
        numOfPlantsTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getNumberOfPlants()));
        plantEnergyTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getPlantEnergy()));
        plantsPerDayTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getPlantsPerDay()));

        forestedEquatorRadioButton.setSelected(presetConfigurations.get(selectedConfiguration).isForestedEquator());
        lifeGivingCorpsesRadioButton.setSelected(!presetConfigurations.get(selectedConfiguration).isForestedEquator());

        numOfAnimalsTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getNumberOfAnimals()));
        animalEnergyTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getAnimalEnergy()));
        reproductionReadyEnergyTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getReproductionReadyEnergy()));
        usedReproductionEnergyTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getUsedReproductionEnergy()));
        minimalMutationsTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getMinimalMutations()));
        maximalMutationsTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getMaximalMutations()));
        genomeLengthTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).getGenomeLength()));

        fullRandomnessRadioButton.setSelected(presetConfigurations.get(selectedConfiguration).isFullRandomnessGenome());
        lightCorrectionRadioButton.setSelected(!presetConfigurations.get(selectedConfiguration).isFullRandomnessGenome());
        }

    public void onSaveConfigClicked() {
        int mapHeight = Integer.parseInt(mapHeightTextField.getText());
        int mapWidth = Integer.parseInt(mapWidthTextField.getText());
        int numberOfPlants = Integer.parseInt(numOfPlantsTextField.getText());
        int plantEnergy = Integer.parseInt(plantEnergyTextField.getText());
        int plantsPerDay = Integer.parseInt(plantsPerDayTextField.getText());

        boolean forestedEquator;
        RadioButton selectedPlantRadioButton = (RadioButton) plantsToggleGroup.getSelectedToggle();
        String plantRadioButtonValue = selectedPlantRadioButton.getText();
        System.out.println(plantRadioButtonValue);
        forestedEquator = plantRadioButtonValue.equals("Zalesiony rownik");

        int numberOfAnimals = Integer.parseInt(numOfAnimalsTextField.getText());
        int animalEnergy = Integer.parseInt(animalEnergyTextField.getText());
        int reproductionReadyEnergy = Integer.parseInt(reproductionReadyEnergyTextField.getText());
        int usedReproductionEnergy = Integer.parseInt(usedReproductionEnergyTextField.getText());
        int minimalMutations = Integer.parseInt(minimalMutationsTextField.getText());
        int maximalMutations = Integer.parseInt(maximalMutationsTextField.getText());
        int genomeLength = Integer.parseInt(genomeLengthTextField.getText());

        boolean fullRandomnessGenome;
        RadioButton selectedGenomeRadioButton = (RadioButton) genomeToggleGroup.getSelectedToggle();
        String genomeRadioButtonValue = selectedGenomeRadioButton.getText();
        fullRandomnessGenome = genomeRadioButtonValue.equals("Pelna losowosc");
        SimulationConfiguration newConfiguration = new SimulationConfiguration(
                mapHeight,
                mapWidth,
                numberOfPlants,
                plantEnergy,
                plantsPerDay,
                forestedEquator,
                numberOfAnimals,
                animalEnergy,
                reproductionReadyEnergy,
                usedReproductionEnergy,
                minimalMutations,
                maximalMutations,
                genomeLength,
                fullRandomnessGenome
        );

        presetConfigurations.put(newConfiguration.toString(), newConfiguration);
        presetConfigurationsComboBox.getItems().add(newConfiguration);
        presetConfigurationsComboBox.getSelectionModel().select(newConfiguration);
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());

        /* TODO
         *   po zamknięciu okienka symulacji powinna zostać przerywana,
         *   a dalej consoleMapDisplay wypisuje ruchy itp, why????? */
        primaryStage.setOnCloseRequest(event -> primaryStage.close());
    }
}
