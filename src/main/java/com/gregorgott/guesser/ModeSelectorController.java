package com.gregorgott.guesser;

import com.gregorgott.mdialogwindows.MDialogWindow;
import com.gregorgott.mdialogwindows.MMultiInformationAlert;
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
 * @version 1.0.1
 * @since 2022-06-16
 */
public class ModeSelectorController {
    private GameMode gameMode;

    /**
     * Sets the game mode to <code>CLASSIC</code> and loads the main menu.
     *
     * @param event action event to get Stage.
     * @throws IOException if the fxml file was not found.
     * @since 1.0.0
     */
    @FXML
    private void classicMode(ActionEvent event) throws IOException {
        gameMode = GameMode.CLASSIC;
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
     * Loads the main menu and passes over the gamer mode.
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

    public void aboutButton(ActionEvent event) {
        MMultiInformationAlert mMultiInformationAlert = new MMultiInformationAlert("About", ((Node) event.getSource()).getScene().getWindow());
        mMultiInformationAlert.setMAlertStyle(MDialogWindow.MAlertStyle.DARK_ROUNDED);
        mMultiInformationAlert.setHeadline("About Word Guesser");
        mMultiInformationAlert.setContentText("Word Guesser is a two player word guessing game, also called 'Hängemännchen' in german.");
        mMultiInformationAlert.addWidget("What is Word Guesser?", "In Word Guesser one player gives a word that the other player needs to guess." +
                "The player may enter single letters to find the word. However, the round ends if the player enters too many characters which are not in the word.");
        mMultiInformationAlert.addWidget("Updates", "Currently Word Guesser does not has an automatic update" +
                " function. However, you can check out the Word Guesser site on GitHub to download the latest version.");
        mMultiInformationAlert.addWidget("License", "This project is licensed under GNU General Public License v3.0.");
        mMultiInformationAlert.addButton("OK", x -> mMultiInformationAlert.closeAlert(), true);
        mMultiInformationAlert.show();
    }
}