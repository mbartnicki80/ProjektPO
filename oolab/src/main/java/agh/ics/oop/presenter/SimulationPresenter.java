package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationPresenter {
    @FXML
    private TextField movementsListTextField;
    private final ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();

    public void onSimulationStartClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation_view.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationViewPresenter presenter = loader.getController();
        Stage stage = new Stage();
        configureStage(stage, viewRoot);

        String moveList = movementsListTextField.getText();
        String[] moves = moveList.split("");
        List<MoveDirection> directions;
        directions = OptionsParser.convertStringToMoveDirection(moves);
        ArrayList<Vector2d> positions = new ArrayList<>(Arrays.asList(new Vector2d(2, 2), new Vector2d(3, 4)));
        GrassField worldMap = new GrassField(10);
        worldMap.addObserver(presenter);
        worldMap.addObserver(consoleMapDisplay);
        presenter.setWorldMap(worldMap);
        //kontrolka wysokość i szerokość mapy
        //kontrolka startowa liczba roślin,
        //kontrolka energia zapewniana przez zjedzenie jednej rośliny,
        //liczba roślin wyrastająca każdego dnia,
        //wariant wzrostu roślin !!kontrolka rozwijana
        //startowa liczba zwierzaków
        int energy = 10; //przerobic na kontrolke jako startowa energia zwierzakow
        //energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania),
        //energia rodziców zużywana by stworzyć potomka,
        //minimalna i maksymalna liczba mutacji u potomków (może być równa 0)
        //wariant mutacji !!kontrolka rozwijana
        int genomeLength = 10; //przerobic na kontrolke

        Simulation simulation = new Simulation(positions, directions, worldMap, energy, genomeLength);
        ArrayList<Simulation> simulations = new ArrayList<>(List.of(simulation));
        SimulationEngine multipleSimulations = new SimulationEngine(simulations);
        multipleSimulations.runAsync();

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
