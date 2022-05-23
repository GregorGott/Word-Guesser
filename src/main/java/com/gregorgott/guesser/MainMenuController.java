package com.gregorgott.guesser;

import com.gregorgott.guesser.panes.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the main menu of Word Guesser. Here the player can select the amount of questions and rounds and switch
 * between single and multiplayer mode.
 *
 * @author GregorGott
 * @version 1.1.4
 * @since 2022-05-23
 */
public class MainMenuController implements Initializable {
    // Single-, Multiplayer toggle buttons
    @FXML
    private ToggleButton multiplayerTogglePlayer;
    @FXML
    private ToggleButton singleplayerTogglePlayer;

    // Declare FXML Spinners
    @FXML
    private Spinner<Integer> numberOfQuestionsSpinner;
    @FXML
    private Spinner<Integer> maxMistakesSpinner;

    private GameMode gameMode;

    /**
     * Sets the game mode.
     * @param gameMode the game mode.
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * @return Number of questions from the Spinner.
     */
    private int getNumberOfQuestionsSpinnerValue() {
        return numberOfQuestionsSpinner.getValue();
    }

    /**
     * @return Number of max mistakes from the Spinner.
     */
    private int getMaxMistakesSpinnerValue() {
        return maxMistakesSpinner.getValue();
    }

    /**
     * Get selected Toggle Button.
     *
     * @return The selected GameType (Singleplayer or Multiplayer).
     */
    private GuesserGameController.GameType getGameType() {
        if (singleplayerTogglePlayer.isSelected()) {
            return GuesserGameController.GameType.SINGLEPLAYER;
        } else {
            return GuesserGameController.GameType.MULTIPLAYER;
        }
    }

    /**
     * Get selected game type. Show a file chooser, if the Singleplayer mode is selected.
     * Starts the game by loading the game FXML file and passing over attributes to the gameController.
     *
     * @param event Get Source as Node of the button to load GuesserGameController on the Stage.
     * @throws IOException Exceptions while loading the FXML file.
     */
    public void startGame(ActionEvent event) throws IOException {
        // Get current Stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("guesser-game-scene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        GuesserGameController guesserGameController = loader.getController();

        if (getGameType() == GuesserGameController.GameType.SINGLEPLAYER) {
            FileManager fileManager = new FileManager();
            File file = fileManager.selectSingleplayerFile(stage.getScene().getWindow());

            if (file != null) {
                guesserGameController.setPathToGuessingFile(file);

                guesserGameController.startGame(getNumberOfQuestionsSpinnerValue(), getMaxMistakesSpinnerValue(),
                        getGameType());

                stage.setScene(scene);
            }
        } else {
            guesserGameController.startGame(getNumberOfQuestionsSpinnerValue(), getMaxMistakesSpinnerValue(),
                    getGameType());

            stage.setScene(scene);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Multiplayer is selected by default
        multiplayerTogglePlayer.setSelected(true);

        // Toggle Group with mode buttons
        ToggleGroup modeButtons = new ToggleGroup();
        modeButtons.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        modeButtons.getToggles().addAll(singleplayerTogglePlayer, multiplayerTogglePlayer);

        // Spinner value factories
        numberOfQuestionsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 6, 2));
        maxMistakesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 13, 8));
    }
}