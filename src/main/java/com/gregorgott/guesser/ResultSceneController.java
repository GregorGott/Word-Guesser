package com.gregorgott.guesser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * At the end of a round a Scene with the winner is displayed. This Controller controls this Scene.
 *
 * @author GregorGott
 * @version 1.1.0
 * @since 2022-05-03
 */
public class ResultSceneController {
    @FXML
    private Label winnerLabel;
    @FXML
    private PieChart pieChart;

    /**
     * Set the winner label.
     *
     * @param text Text to show in the winnerLabel.
     */
    public void setWinnerLabel(String text) {
        winnerLabel.setText(text);
    }

    /**
     * Set a pie chart with the points of the player(s).
     *
     * @param i1      (Multiplayer) Points of player 1. (Singleplayer) Guessed words.
     * @param i2      (Multiplayer) Points of player 2. (Singleplayer) Not guessed words.
     * @param legend1 Legend for i1.
     * @param legend2 Legend for i2
     */
    public void setPieChart(int i1, int i2, String legend1, String legend2) {
        if (0 < i1 && 0 < i2) {
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data(legend1, i1),
                            new PieChart.Data(legend2, i2));
            pieChart.setData(pieChartData);
        } else {
            pieChart.setVisible(false);
        }
    }

    public void backToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-menu-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}