package com.gregorgott.guesser.SetQuestionPanes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
public class SimpleSetQuestionPane extends SetQuestionManager {
    private final TextField textField;

    public SimpleSetQuestionPane() {
        textField = new TextField("Caretaker");
    }

    /**
     * Counts the number of letters and filters out duplicates.
     *
     * @return the number of letters (without duplicates).
     */
    private int countPointsInTextField() {
        List<Character> characters = new ArrayList<>();

        int y = 0;

        for (int i = 0; i < textField.getLength(); i++) {
            char c = textField.getText().charAt(i);
            if (!characters.contains(c)) {
                characters.add(c);
                y++;
            }
        }

        return y;
    }

    /**
     * @return the text in the <code>textField</code>.
     */
    @Override
    public String getInput() {
        return textField.getText();
    }

    /**
     * Creates two labels and a text field. The text field is used to enter the word to be guessed by the other
     * player.
     *
     * @return a HBox with the text field label, text field and character counter label.
     * @since 1.1.0
     */
    @Override
    public Node getNode() {
        Label label = new Label("Enter a word:");
        label.setId("white-label");
        Label characterCounter = new Label();
        characterCounter.setId("white-label");

        textField.setOnKeyTyped(x -> characterCounter.setText("Points: " + countPointsInTextField()));

        characterCounter.setText("Points: " + countPointsInTextField());

        HBox hBox = new HBox(label, textField, characterCounter);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(20));

        return hBox;
    }
}
