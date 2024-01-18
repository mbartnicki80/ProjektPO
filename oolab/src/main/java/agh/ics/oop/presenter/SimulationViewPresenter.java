package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import java.util.*;

public class SimulationViewPresenter implements MapChangeListener {
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label movementsDescriptionLabel;
    @FXML
    private Button stopButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Button showMapStatsButton;
    @FXML
    private Button hideMapStatsButton;
    @FXML
    private Label mapStatsLabel;
    @FXML
    private Label animalStatsLabel;
    @FXML
    private Button hideAnimalStatsButton;
    @FXML
    private Button startHighlightingGenomeButton;
    @FXML
    private Button stopHighlightingGenomeButton;
    @FXML
    private Button startHighlightingPreferablePlantPositionsButton;
    @FXML
    private Button stopHighlightingPreferablePlantPositionsButton;

    private Animal currentFollowedAnimal = null;
    private WorldMap worldMap;
    private MapWithStatistics mapStats;
    private Simulation simulation;
    private boolean highlightPositions = false;
    private boolean higlightGenome = false;
    private boolean showMapStatsActive = false;
    private final static int CELL_SIZE = 40;

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
        this.mapStats = map;
    }
    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }


    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void drawHeaders() {
        Boundary bounds = worldMap.getCurrentBounds();
        int lowerLeftX = bounds.lowerLeft().getXValue();
        int lowerLeftY = bounds.lowerLeft().getYValue();
        int upperRightX = bounds.upperRight().getXValue();
        int upperRightY = bounds.upperRight().getYValue();

        for (int row = lowerLeftY; row <= upperRightY; row++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
            Label label = new Label();
            label.setText(String.valueOf(row));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, upperRightY - row + 1);
        }

        for (int column = lowerLeftX; column <= upperRightX; column++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
            Label label = new Label();
            label.setText(String.valueOf(column));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, column + 1 - lowerLeftX, 0);
        }

        mapGrid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
        Label label = new Label();
        label.setText(" y\\x ");
        GridPane.setHalignment(label, HPos.CENTER);
        mapGrid.add(label, 0, 0);
    }

    private void drawBackround() {
        Boundary bounds = worldMap.getCurrentBounds();
        int lowerLeftX = bounds.lowerLeft().getXValue();
        int lowerLeftY = bounds.lowerLeft().getYValue();
        int upperRightX = bounds.upperRight().getXValue();
        int upperRightY = bounds.upperRight().getYValue();

        for (int i = lowerLeftX; i <= upperRightX; i++) {
            for (int j = lowerLeftY; j <= upperRightY; j++) {
                StackPane cellContainer = new StackPane();
                Region background = new Region();
                background.setStyle("-fx-background-color: #D2B48C;");
                background.setMaxSize(CELL_SIZE-1, CELL_SIZE-1);
                cellContainer.getChildren().add(background);
                mapGrid.add(cellContainer, i - lowerLeftX + 1, upperRightY - j + 1);
            }
        }
    }

    private void drawPreferredPositions() {
        Boundary bounds = worldMap.getCurrentBounds();
        int lowerLeftX = bounds.lowerLeft().getXValue();
        int upperRightY = bounds.upperRight().getYValue();

        for (Vector2d position : worldMap.getPreferredPositions()) {
            StackPane cellContainer = new StackPane();
            Region background = new Region();
            background.setStyle("-fx-background-color: #00FF00;");
            background.setMaxSize(CELL_SIZE-1, CELL_SIZE-1);
            cellContainer.getChildren().add(background);
            mapGrid.add(cellContainer, position.getXValue() - lowerLeftX + 1, upperRightY - position.getYValue() + 1);
        }
    }

    private void drawMap() {
        clearGrid();
        drawHeaders();
        drawBackround();

        Boundary bounds = worldMap.getCurrentBounds();
        int lowerLeftX = bounds.lowerLeft().getXValue();
        int lowerLeftY = bounds.lowerLeft().getYValue();
        int upperRightX = bounds.upperRight().getXValue();
        int upperRightY = bounds.upperRight().getYValue();

        if (currentFollowedAnimal != null)
            showAnimalStats(currentFollowedAnimal);
        if (showMapStatsActive)
            showMapStats();

        if (highlightPositions)
            drawPreferredPositions();

        for (int i = lowerLeftX; i <= upperRightX; i++) {
            for (int j = lowerLeftY; j <= upperRightY; j++) {
                Optional<WorldElement> worldElement = worldMap.objectAt(new Vector2d(i, j));

                if (worldElement.isPresent()) {
                    WorldElementBox worldElementBox = new WorldElementBox(worldElement.get());

                    if (worldElement.get() instanceof Animal animal) {
                        if (higlightGenome && animal.getGenome()==mapStats.getDominantGenome()) {
                            StackPane cellContainer = new StackPane();
                            Region background = new Region();
                            background.setStyle("-fx-background-color: #0000FF;");
                            background.setMaxSize(CELL_SIZE-1, CELL_SIZE-1);
                            cellContainer.getChildren().add(background);
                            mapGrid.add(cellContainer, animal.position().getXValue() - lowerLeftX + 1, upperRightY - animal.position().getYValue() + 1);
                        }
                        Label elemLabel = new Label(Integer.toString(animal.getEnergy()));
                        mapGrid.add(elemLabel, i - lowerLeftX + 1, upperRightY - j + 1);
                        GridPane.setHalignment(elemLabel, HPos.CENTER);
                        GridPane.setValignment(elemLabel, VPos.BOTTOM);
                    }

                    if (!simulation.getRunningStatus()) {
                        worldElementBox.getVBox().setOnMouseClicked(event -> {
                            if (worldElement.get() instanceof Animal animal) {
                                currentFollowedAnimal = animal;
                                showAnimalStats(animal);
                            }
                        });
                    }

                    mapGrid.add(worldElementBox.getVBox(), i - lowerLeftX + 1, upperRightY - j + 1);
                    GridPane.setHalignment(worldElementBox.getVBox(), HPos.CENTER);
                    GridPane.setValignment(worldElementBox.getVBox(), VPos.CENTER);
                }
            }
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            movementsDescriptionLabel.setText(message);
        });
    }

    public void onStartHighlightingGenomeClicked() {
        if (simulation.getRunningStatus())
            return;
        startHighlightingGenomeButton.setVisible(false);
        stopHighlightingGenomeButton.setVisible(true);
        higlightGenome = true;
        drawMap();
    }

    public void onStopHighlightingGenomeClicked() {
        startHighlightingGenomeButton.setVisible(true);
        stopHighlightingGenomeButton.setVisible(false);
        higlightGenome = false;
        drawMap();
    }

    public void onStartHighlightingPositionsClicked() {
        if (simulation.getRunningStatus())
            return;

        startHighlightingPreferablePlantPositionsButton.setVisible(false);
        stopHighlightingPreferablePlantPositionsButton.setVisible(true);
        highlightPositions = true;
        drawMap();
    }

    public void onStopHighlightingPositionsClicked() {
        startHighlightingPreferablePlantPositionsButton.setVisible(true);
        stopHighlightingPreferablePlantPositionsButton.setVisible(false);
        highlightPositions = false;
        drawMap();
    }

    public void onSimulationStopClicked() {
        stopButton.setVisible(false);
        resumeButton.setVisible(true);
        this.simulation.pauseSimulation();
        startHighlightingGenomeButton.setVisible(true);
        startHighlightingPreferablePlantPositionsButton.setVisible(true);
    }

    public void onSimulationResumeClicked() {
        stopButton.setVisible(true);
        resumeButton.setVisible(false);
        this.simulation.resumeSimulation();
        startHighlightingGenomeButton.setVisible(true);
        stopHighlightingGenomeButton.setVisible(false);
        startHighlightingPreferablePlantPositionsButton.setVisible(true);
        stopHighlightingPreferablePlantPositionsButton.setVisible(false);
        higlightGenome = false;
        highlightPositions = false;
        drawMap();
    }

    public void onShowMapStatsClicked() {
        showMapStatsButton.setVisible(false);
        hideMapStatsButton.setVisible(true);

        mapStatsLabel.setVisible(true);
        showMapStatsActive = true;
        showMapStats();
    }

    public void onHideMapStatsClicked() {
        showMapStatsButton.setVisible(true);
        hideMapStatsButton.setVisible(false);

        mapStatsLabel.setVisible(false);
        showMapStatsActive = false;
    }

    public void onHideAnimalStatsClicked() {
        animalStatsLabel.setVisible(false);
        hideAnimalStatsButton.setVisible(false);
        currentFollowedAnimal = null;
    }

    private void showAnimalStats(Animal animal) {
        String animalInfo = getAnimalStats(animal);
        animalStatsLabel.setText(animalInfo);
        animalStatsLabel.setVisible(true);
        hideAnimalStatsButton.setVisible(true);
    }

    private String getAnimalStats(Animal animal) {
        return "Informacje o zwierzaku:\n" +
                "Status: " + (animal.isDead() ? "Martwy" : "Zywy") + "\n" +
                "Pozycja: " + animal.position() + "\n" +
                "Urodzony dnia: " + animal.getDayOfBirth() + "\n" +
                "Genom: " + animal.getGenome() + "\n" +
                "Obecny gen aktywny: " + animal.getGenome().getCurrentGenome() + "\n" +
                "Energia: " + animal.getEnergy() + "\n" +
                "Zjedzone rosliny: " + animal.getPlantsEatenCount() + "\n" +
                "Liczba dzieci: " + animal.getChildrenCount() + "\n" +
                "Liczba potomkow: " + animal.getDescendantsNumber() + "\n" +
                "Liczba zywych potomkow: " + animal.getAliveDescendantsNumber() + "\n" +
                (animal.isDead() ? "Zmarl dnia: " + animal.getDayOfDeath() : ("Zyje juz: " + (mapStats.getDay() - animal.getDayOfBirth()))
                + ((mapStats.getDay() - animal.getDayOfBirth()) == 1 ? " dzien" : " dni"));
    }

    private void showMapStats() {
        mapStatsLabel.setText(getMapStats());
    }

    private String getMapStats() {
        return "Dzien: " + mapStats.getDay() + "\n" +
                "Liczba zywych zwierzat: " + mapStats.getNumberOfAnimals() + "\n" +
                "Liczba roslin: " + mapStats.getNumberOfPlants() + "\n" +
                "Wolne miejsce: " + mapStats.getFreeSpace() + "\n" +
                "Dominujacy genom: " + "\n" + mapStats.getDominantGenome() + "\n" +
                "Srednia energia zwierzakow: " + mapStats.getAverageEnergy() + "\n" +
                "Srednia dlugosc zycia martwych zwierzat: " + mapStats.getAverageLifeLengthOfDeadAnimals() + "\n" +
                "Srednia liczba dzieci zyjacych zwierzakow: " + mapStats.getAverageChildrenCount();
    }


}