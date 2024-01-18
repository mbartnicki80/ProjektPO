package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
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
    private ComboBox<SimulationConfiguration> presetConfigurationsComboBox;
    @FXML
    private RadioButton forestedEquatorRadioButton;
    @FXML
    private RadioButton lifeGivingCorpsesRadioButton;
    @FXML
    private RadioButton fullRandomnessRadioButton;
    @FXML
    private RadioButton lightCorrectionRadioButton;
    @FXML
    private TextField simulationNameToSaveTextField;
    @FXML
    private CheckBox statsToCSVCheckBox;

    private final Map<String, SimulationConfiguration> presetConfigurations = new HashMap<>();
    {
        SimulationConfiguration config1 = new SimulationConfiguration(10, 10, 5, 5, 2,
                true, 2, 10, 5, 2, 5,
                10, 7, true, "Konfiguracja 1");
        SimulationConfiguration config2 = new SimulationConfiguration(8, 9, 3, 2, 1,
                false, 1, 15, 25, 23, 5,
                10, 7, false, "Konfiguracja 2");
        presetConfigurations.put(config1.configurationName, config1);
        presetConfigurations.put(config2.configurationName, config2);
    }
    private final ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
    //private final FileMapDisplay fileMapDisplay = new FileMapDisplay();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void initialize() {
        File dir = new File("oolab/save");
        File[] files = dir.listFiles(file -> file.getName().endsWith(".xml") && file.isFile());
        if (files != null) {
            for (File file : files) {
                SimulationConfiguration configuration = SimulationConfiguration.decodeFromXML(file.getAbsolutePath());
                presetConfigurations.put(Objects.requireNonNull(configuration).configurationName, configuration);
            }
        } else {
            System.out.println("Saves are null");
        }
        presetConfigurationsComboBox.getItems().addAll(presetConfigurations.values());
    }

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

        validateInput(mapHeight, mapWidth, numberOfPlants, plantEnergy, plantsPerDay, numberOfAnimals, animalEnergy,
                reproductionReadyEnergy, usedReproductionEnergy, minimalMutations, maximalMutations, genomeLength);

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
        boolean isCheckBoxSelected = statsToCSVCheckBox.isSelected();
        presenter.setStatsToCSV(isCheckBoxSelected);

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
        String selectedConfiguration = presetConfigurationsComboBox.getValue().configurationName;

        mapHeightTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).mapHeight));
        mapWidthTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).mapWidth));
        numOfPlantsTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).numberOfPlants));
        plantEnergyTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).plantEnergy));
        plantsPerDayTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).plantsPerDay));

        forestedEquatorRadioButton.setSelected(presetConfigurations.get(selectedConfiguration).forestedEquator);
        lifeGivingCorpsesRadioButton.setSelected(!presetConfigurations.get(selectedConfiguration).forestedEquator);

        numOfAnimalsTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).numberOfAnimals));
        animalEnergyTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).animalEnergy));
        reproductionReadyEnergyTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).reproductionReadyEnergy));
        usedReproductionEnergyTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).usedReproductionEnergy));
        minimalMutationsTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).minimalMutations));
        maximalMutationsTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).maximalMutations));
        genomeLengthTextField.setText(String.valueOf(presetConfigurations.get(selectedConfiguration).genomeLength));

        fullRandomnessRadioButton.setSelected(presetConfigurations.get(selectedConfiguration).fullRandomnessGenome);
        lightCorrectionRadioButton.setSelected(!presetConfigurations.get(selectedConfiguration).fullRandomnessGenome);
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

        String newPath = simulationNameToSaveTextField.getText();
        if (newPath.isEmpty()) {
            System.out.println("Podaj nazwe pliku.");
            return;
        }
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
                fullRandomnessGenome,
                newPath
        );

        presetConfigurations.put(newConfiguration.configurationName, newConfiguration);
        presetConfigurationsComboBox.getItems().add(newConfiguration);
        presetConfigurationsComboBox.getSelectionModel().select(newConfiguration);

        SimulationConfiguration.encodeToXML("oolab/save/" + newPath + ".xml", newConfiguration);
    }

    private void validateInput(int mapHeight, int mapWidth, int numberOfPlants, int plantEnergy, int plantsPerDay, int numberOfAnimals,
                               int animalEnergy, int reproductionReadyEnergy, int usedReproductionEnergy,
                               int minimalMutations, int maximalMutations, int genomeLength) {

        if (mapHeight < 1 || mapHeight > 23)
            throw new ArgumentsValidationException("Wysokosc mapy musi byc z przedzialu [1, 23]");
        if (mapWidth < 1 || mapWidth > 40)
            throw new ArgumentsValidationException("Szerokosc mapy musi byc z przedzialu [1, 40]");
        if (numberOfPlants < 0 || numberOfPlants > mapHeight * mapWidth)
            throw new ArgumentsValidationException("Liczba roslin musi byc z przedzialu [0, pole mapy]");
        if (plantEnergy < 0)
            throw new ArgumentsValidationException("Energia rosliny musi byc nieujemna");
        if (plantsPerDay < 0)
            throw new ArgumentsValidationException("Liczba roslin na dzien musi byc nieujemna");
        if (numberOfAnimals < 0 || numberOfAnimals > mapHeight * mapWidth)
            throw new ArgumentsValidationException("Liczba zwierzat musi byc z przedzialu [0, pole mapy]");
        if (animalEnergy < 0)
            throw new ArgumentsValidationException("Energia zwierzecia musi byc nieujemna");
        if (reproductionReadyEnergy < 0)
            throw new ArgumentsValidationException("Energia potrzebna do rozmnazania musi byc nieujemna");
        if (usedReproductionEnergy < 0)
            throw new ArgumentsValidationException("Energia zuzywana podczas rozmnazania musi byc nieujemna");
        if (minimalMutations < 0)
            throw new ArgumentsValidationException("Minimalna liczba mutacji musi byc nieujemna");
        if (maximalMutations < 0 || maximalMutations < minimalMutations)
            throw new ArgumentsValidationException("Maksymalna liczba mutacji musi byc nieujemna i wieksza lub rowna minimalnej liczby mutacji");
        if (genomeLength < 0)
            throw new ArgumentsValidationException("Dlugosc genomu musi byc nieujemna");

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
