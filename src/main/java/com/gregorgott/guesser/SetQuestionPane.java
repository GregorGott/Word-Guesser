package com.gregorgott.guesser;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Show a pane to enter a word to be guessed.
 *
 * @author GregorGott
 * @version 1.0.0
 * @since 2022-04-10
 */
public class SetQuestionPane {

    private final VBox vBox;

    public TextField textField;

    /**
     * Shows a <code>textField</code> in which the player enters a word. This word tries to guess the other player.
     * The word needs to be longer than two characters. All elements are in one VBox.
     */
    public SetQuestionPane() {
        textField = new TextField("Caretaker");

        Label label = new Label("Enter a word:");

        vBox = new VBox();
        vBox.setPadding(new Insets(15, 15, 15, 15));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(label, textField);
    }

    /**
     * @return A VBox with all elements
     */
    public VBox getPane() {
        return vBox;
    }

    /**
     * @return The text from the text field.
     */
    public String getTextFieldText() {
        return textField.getText();
    }
}