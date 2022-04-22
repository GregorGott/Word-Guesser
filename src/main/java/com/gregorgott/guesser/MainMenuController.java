package com.gregorgott.guesser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The first Scene to be shown when the application starts. Here the user can set the max amount of mistakes
 * and number of questions.
 *
 * @author GregorGott
 * @version 1.1.0
 * @since 2022-04-22
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

    private GuesserGameController guesserGameController;

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
     * Starts the game by loading the game fxml file and pass over attributes to the gameController.
     *
     * @param event Get Source as Node of button to load GuesserGameController on the Stage.
     * @throws IOException Exceptions while loading the FXML file.
     */
    public void startGame(ActionEvent event) throws IOException {
        FileManager fileManager = new FileManager();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("guesser-game-scene.fxml"));
        Parent root = loader.load();
        guesserGameController = loader.getController();

        // Get current Stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (getGameType() == GuesserGameController.GameType.SINGLEPLAYER && isSelectFileFromComputer()) {
            // If the user want to select a file from the computer
            File file = fileManager.selectFile();

            // The file needs more than one line
            if (fileManager.countLines(file, "##") < 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File not valid");
                alert.setHeaderText("Please select another file");
                alert.setContentText("The file needs to has more than one line.");
                alert.show();
            } else {
                guesserGameController.setPathToGuessingFile(file);
                loadGameScene(root, stage);
            }
        } else if (getGameType() == GuesserGameController.GameType.MULTIPLAYER) {
            loadGameScene(root, stage);
        }
    }

    /**
     * Call the GuesserGameController start method with number of question, number of max mistakes and the game type
     * and load root in a Stage.
     * @param root  Content of the FXML file.
     * @param stage Stage to load the FXML on it.
     */
    private void loadGameScene(Parent root, Stage stage) {
        guesserGameController.startGame(getNumberOfQuestionsSpinnerValue(), getMaxMistakesSpinnerValue(),
                getGameType());

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * Return a boolean if the user want to select a text file from his computer.
     * @return A boolean if the user want to select a text file from his computer.
     */
    private boolean isSelectFileFromComputer() {
        ButtonType copyLinkButton = new ButtonType("Copy link");
        ButtonType selectFileButton = new ButtonType("Select File");

        // Show alert with information message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Singleplayer Game");
        alert.setHeaderText("How to use Singleplayer?");
        alert.setContentText("Word Guesser needs a file with random words you can guess. Each line is a new word." +
                "You can download a file on GitHub or select a file on your hard-drive.");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(copyLinkButton, selectFileButton, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(null) == selectFileButton) {
            // If user want to open a file on his computer
            return true;
        } else  if (result.orElse(null) == copyLinkButton) {
            // Copy link to download to clipboard
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putUrl("https://github.com/GregorGott/WG-Singleplayer-Files");

            Clipboard clipboard = Clipboard.getSystemClipboard();
            clipboard.setContent(clipboardContent);
        } else if (result.orElse(null) == ButtonType.CANCEL) {
            // Close alert
            alert.close();
        }

        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Multiplayer is selected by default
        multiplayerTogglePlayer.setSelected(true);

        // Toggle Group with mode buttons
        ToggleGroup modeButtons = new ToggleGroup();
        modeButtons.getToggles().addAll(singleplayerTogglePlayer, multiplayerTogglePlayer);

        // Spinner value factories
        numberOfQuestionsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 6, 2));
        maxMistakesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 13, 8));
    }
}