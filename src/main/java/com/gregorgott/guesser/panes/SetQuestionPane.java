package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Show a pane to enter a word to be guessed.
 *
 * @author GregorGott
 * @version 1.0.5
 * @since 2022-05-17
 */
public class SetQuestionPane {
    private final Node root;
    private final TextField wordToGuessTextField;

    /**
     * Shows a VBox with one text field in which the player enters a random word and a text field which shows a
     * character counter for the text field.
     */
    public SetQuestionPane() {
        // Enter a word
        Label label = new Label("Enter a word:");
        label.setId("white-label");

        Label characterCounter = new Label();
        characterCounter.setId("white-label");

        wordToGuessTextField = new TextField("Caretaker");
        wordToGuessTextField.setOnKeyTyped(x -> characterCounter.setText("Points: " + countPointsInTextField()));

        characterCounter.setText("Points: " + countPointsInTextField());

        HBox wordToGuessHBox = new HBox(label, wordToGuessTextField, characterCounter);
        wordToGuessHBox.setAlignment(Pos.CENTER_LEFT);
        wordToGuessHBox.setSpacing(20);
        wordToGuessHBox.setPadding(new Insets(20));

        root = new VBox(wordToGuessHBox);
    }

    /**
     * Counts the number of letters and filters out duplicates.
     *
     * @return the number of letters (without duplicates).
     */
    private int countPointsInTextField() {
        List<Character> characters = new ArrayList<>();

        int y = 0;

        for (int i = 0; i < wordToGuessTextField.getLength(); i++) {
            char c = wordToGuessTextField.getText().charAt(i);
            if (!characters.contains(c)) {
                characters.add(c);
                y++;
            }
        }

        return y;
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