package com.gregorgott.guesser;

import com.gregorgott.guesser.panes.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The mode selector is the first window to be shown. It contains a button for each mode.
 * After a button is pressed, it loads the main menu with the selected mode.
 *
 * @author GregorGott
 * @version 1.0.0
 * @since 2022-15-23
 */
public class ModeSelectorController {
    private GameMode gameMode;

    /**
     * Sets the game mode to <code>ORIGINAL</code> and loads the main menu.
     *
     * @param event action event to get Stage.
     * @throws IOException if the fxml file was not found.
     * @since 1.0.0
     */
    @FXML
    private void classicMode(ActionEvent event) throws IOException {
        gameMode = GameMode.ORIGINAL;
        loadMainMenu(event);
    }

    /**
     * Sets the game mode to <code>CARDS</code> and loads the main menu.
     *
     * @param event action event to switch the Stage.
     * @throws IOException if the fxml file was not found.
     * @since 1.0.0
     */
    @FXML
    private void cardsMode(ActionEvent event) throws IOException {
        gameMode = GameMode.CARDS;
        loadMainMenu(event);
    }

    /**
     * Loads the main menu and pass over the gamer mode.
     *
     * @param event action event to switch the Stage.
     * @throws IOException if the fxml file was not found.
     * @since 1.0.0
     */
    private void loadMainMenu(ActionEvent event) throws IOException {
        // Load main-menu-scene.fxml in 'Word Guesser' Stage
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.setGameMode(gameMode);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

    }
}
