package com.gregorgott.guesser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Spinner<Integer> numberOfQuestionsSpinner;

    @FXML
    private Spinner<Integer> maxMistakesSpinner;

    public int getNumberOfQuestionsSpinner() {
        return numberOfQuestionsSpinner.getValue();
    }

    public int getMaxMistakesSpinner() {
        return maxMistakesSpinner.getValue();
    }

    public void startGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("guesser-game-scene.fxml"));
        Parent root = loader.load();

        GuesserGameController gameController = loader.getController();
        gameController.setBasics(getNumberOfQuestionsSpinner(), getMaxMistakesSpinner());

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOfQuestionsSpinner.setEditable(true);
        numberOfQuestionsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2,100, 6, 2));


        maxMistakesSpinner.setEditable(true);
        maxMistakesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,13, 8));
    }
}