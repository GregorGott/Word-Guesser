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

/**
 * The first Scene to be shown when the application starts. Here the user can set the max amount of mistakes
 * and number of questions.
 *
 * @author GregorGott
 * @version 1.0.1
 * @since 2022-04-09
 */
public class MainMenuController implements Initializable {

    // Declare FXML Spinners
    @FXML
    private Spinner<Integer> numberOfQuestionsSpinner;

    @FXML
    private Spinner<Integer> maxMistakesSpinner;

    /**
     * @return Number of questions from the Spinner.
     */
    public int getNumberOfQuestionsSpinnerValue() {
        return numberOfQuestionsSpinner.getValue();
    }

    /**
     * @return Number of max mistakes from the Spinner.
     */
    public int getMaxMistakesSpinnerValue() {
        return maxMistakesSpinner.getValue();
    }

    /**
     * Starts the game by loading the game fxml file and pass over attributes to the <code>gameController</code>.
     *
     * @param event Get Source as Node of button to load <code>GuesserGameController</code> on the Stage.
     * @throws IOException Load FXML file.
     */
    public void startGame(ActionEvent event) throws IOException {
        // Load Game FXML into Scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("guesser-game-scene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Implement GuesserGameController and set number of questions and max mistakes
        GuesserGameController guesserGameController = loader.getController();
        guesserGameController.startGame(getNumberOfQuestionsSpinnerValue(), getMaxMistakesSpinnerValue());

        // Get current Stage and switch Scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOfQuestionsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 6, 2));
        maxMistakesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 13, 8));
    }
}