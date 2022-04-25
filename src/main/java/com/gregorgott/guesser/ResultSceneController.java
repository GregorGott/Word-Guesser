package com.gregorgott.guesser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * At the end of a round a Scene with the winner is displayed. This Controller controls this Scene.
 *
 * @author GregorGott
 * @version 1.0.0
 * @since 2022-04-25
 */
public class ResultSceneController {
    @FXML
    Label winnerLabel;

    public void setWinnerLabel(String text) {
        winnerLabel.setText(text);
    }

    public void backToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-menu-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
