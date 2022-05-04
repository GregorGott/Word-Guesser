package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Show a pane to enter a word to be guessed.
 *
 * @author GregorGott
 * @version 1.0.4
 * @since 2022-04-30
 */
public class SetQuestionPane {

    private final Node root;
    private final TextField wordToGuessTextField;

    /**
     * Shows a VBox with one text field in which the player enters a random word.
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
    public Node getPane() {
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