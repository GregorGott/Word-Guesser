package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Show a pane to enter a word to be guessed.
 *
 * @author GregorGott
 * @version 1.0.3
 * @since 2022-04-29
 */
public class SetQuestionPane {

    private final Parent root;
    private final TextField wordToGuessTextField;

    /**
     * Shows a VBox with to text fields. In wordToGuess the player enters a word and in optionalTipTextField the
     * player enters a optional tip for the other player.
     */
    public SetQuestionPane() {
        // Enter a word
        Label label = new Label("Enter a word:");
        label.setPrefWidth(115);
        label.setId("white-label");

        wordToGuessTextField = new TextField("Caretaker");

        HBox wordToGuessHBox = new HBox(label, wordToGuessTextField);
        wordToGuessHBox.setPadding(new Insets(20));

        root = wordToGuessHBox;
    }

    /**
     * @return Root Parent with all UI elements.
     */
    public Parent getPane() {
        return root;
    }

    /**
     * @return The text field.
     */
    public TextField getWordToGuessTextField() {
        return wordToGuessTextField;
    }

    /**
     * @return The text from the text field.
     */
    public String getWordToBeGuessed() {
        return wordToGuessTextField.getText();
    }
}