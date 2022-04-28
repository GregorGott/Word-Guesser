package com.gregorgott.guesser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Show a pane to enter a word to be guessed.
 *
 * @author GregorGott
 * @version 1.0.2
 * @since 2022-04-25
 */
public class SetQuestionPane {

    private final HBox hBox;
    private final TextField textField;

    /**
     * Shows a text field in which the player enters a word. The other player tries to guess the entered word.
     * The word needs to be longer than two characters. All elements are in one HBox.
     */
    public SetQuestionPane() {
        textField = new TextField("Caretaker");
        HBox.setHgrow(textField, Priority.ALWAYS);

        Label label = new Label("Enter a word:");
        label.setId("white-label");

        hBox = new HBox();
        hBox.setPadding(new Insets(20));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(label, textField);
    }

    /**
     * Get the HBox with the text field and label.
     * @return A HBox with all elements
     */
    public HBox getPane() {
        return hBox;
    }

    /**
     * @return The text field.
     */
    public TextField getTextField() {
        return textField;
    }

    /**
     * @return The text from the text field.
     */
    public String getTextFieldText() {
        return textField.getText();
    }
}