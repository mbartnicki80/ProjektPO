package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationViewPresenter implements MapChangeListener {
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label movementsDescriptionLabel;
    private WorldMap worldMap;
    private final static int CELL_SIZE = 30;

    private List<List<Map<String, WorldElementBox>>> worldElementBoxes;

    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
        this.worldElementBoxes = new ArrayList<>();


        for (int i = 0; i <= worldMap.getCurrentBounds().upperRight().getXValue(); i++) {
            worldElementBoxes.add(new ArrayList<>());
            for (int j = 0; j <= worldMap.getCurrentBounds().upperRight().getYValue(); j++) {
                worldElementBoxes.get(i).add(new ConcurrentHashMap<>());
            }
        }

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
    private void drawMap() {
        clearGrid();
        drawHeaders();
        Boundary bounds = worldMap.getCurrentBounds();
        int lowerLeftX = bounds.lowerLeft().getXValue();
        int lowerLeftY = bounds.lowerLeft().getYValue();
        int upperRightX = bounds.upperRight().getXValue();
        int upperRightY = bounds.upperRight().getYValue();


        Map<String, WorldElementBox> worldElementBoxMap = new HashMap<>();


        for (int i = lowerLeftX; i <= upperRightX; i++) {
            for (int j = lowerLeftY; j <= upperRightY; j++) {
                Optional<WorldElement> worldElement = worldMap.objectAt(new Vector2d(i, j));
                if (worldElement.isPresent()) {

                    WorldElementBox worldElementBox;

                    if (worldElementBoxes.get(i - lowerLeftX).get(upperRightY - j).containsKey(worldElement.get().toString())) {
                        worldElementBox = worldElementBoxes.get(i - lowerLeftX).get(upperRightY - j).get(worldElement.get().toString());
                    } else {
                        worldElementBox = new WorldElementBox(worldElement.get());
                        worldElementBoxes.get(i - lowerLeftX).get(upperRightY - j).put(worldElement.get().toString(), worldElementBox);
                    }


                    Label elemLabel = new Label(worldElement.get().toString());
                    mapGrid.add(worldElementBox.getVBox(), i - lowerLeftX + 1, upperRightY - j + 1);
                    GridPane.setHalignment(elemLabel, HPos.CENTER);
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
}