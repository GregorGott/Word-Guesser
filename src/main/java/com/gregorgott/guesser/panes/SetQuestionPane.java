package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Show a pane to enter a word to be guessed.
 *
 * @author GregorGott
 * @version 1.0.6
 * @since 2022-06-16
 */
public class SetQuestionPane {
    private final TextField wordToGuessTextField;

    public SetQuestionPane() {
        wordToGuessTextField = new TextField("Caretaker");
    }

    /**
     * Creates two labels and a text field. The text field is used to enter the word to be guessed by the other
     * player.
     *
     * @return a HBox with the text field label, text field and character counter label.
     * @since < 1.0.5
     */
    public Node getRoot() {
        // Enter a word
        Label label = new Label("Enter a word:");
        label.setId("white-label");

        Label characterCounter = new Label();
        characterCounter.setId("white-label");

        wordToGuessTextField.setOnKeyTyped(x -> characterCounter.setText("Points: " + countPointsInTextField()));

        characterCounter.setText("Points: " + countPointsInTextField());

        HBox wordToGuessHBox = new HBox(label, wordToGuessTextField, characterCounter);
        wordToGuessHBox.setSpacing(20);
        wordToGuessHBox.setPadding(new Insets(20));

        return wordToGuessHBox;
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
     * @return the text field.
     */
    public TextField getTextField() {
        return wordToGuessTextField;
    }
}