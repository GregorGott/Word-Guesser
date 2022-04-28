package com.gregorgott.guesser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * Show a pane where the player tries to guess the given word. And control the mistakes circles. Each Mistake is one
 * circle.
 *
 * @author GregorGott
 * @version 1.0.2
 * @since 2022-04-25
 */
public class AskQuestionPane {
    private final VBox mainVBox;
    private final HBox circleHBox;
    private final List<Character> usedCharsList;
    private final Button checkGuessButton, showTipButton;
    private final Label outputLabel, usedCharsLabel;
    private final TextField textField;

    /**
     * Set all elements for the UI. Create a text field in which single letters are written to guess the word.
     * Each character in the outputLabelArray is one underscore in the scroll pane.
     *
     * @param outputLabelArray Each character is an underscore.
     * @param maxMistakes      Max amount of mistakes. Each mistake is one circle.
     * @see <a href="https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength">JavaFX 2.2 TextField maxlength</a>
     */
    public AskQuestionPane(char[] outputLabelArray, int maxMistakes) {
        usedCharsList = new ArrayList<>();

        Label enterACharLabel = new Label("Enter a character:");
        enterACharLabel.setId("white-label");

        textField = new TextField();
        // The listener avoids entering more than one character
        textField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (textField.getText().length() > 1) {
                String s = textField.getText().substring(0, 1);
                textField.setText(s);
            }
        });
        HBox.setHgrow(textField, Priority.ALWAYS);

        // Check button
        checkGuessButton = new Button("Check Guess");

        // textField and checkGuessButton are in a guessHBox
        HBox guessHBox = new HBox();
        guessHBox.setAlignment(Pos.CENTER_LEFT);
        guessHBox.setSpacing(10);
        guessHBox.getChildren().addAll(enterACharLabel, textField, checkGuessButton);

        // Underline/Underscore for each word in "solutionArray"
        outputLabel = new Label();
        for (char c : outputLabelArray) {
            outputLabel.setText(outputLabel.getText() + " " + c + " ");
        }

        // ScrollPane to scroll if the word is too long
        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setPrefHeight(50);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPadding(new Insets(10));

        Label mistakesLabel = new Label("Mistakes:");
        mistakesLabel.setId("white-label");

        // HBox which contains mistake circles
        circleHBox = new HBox();
        circleHBox.setAlignment(Pos.CENTER_LEFT);
        circleHBox.setSpacing(8);

        // Create circle for each available mistake
        for (int i = 0; i < maxMistakes; i++) {
            addCircle(Color.rgb(33, 145, 27));
        }

        // HBox with enterACharLabel and mistake circles
        HBox mistakeCirclesHBox = new HBox();
        mistakeCirclesHBox.setSpacing(10);
        mistakeCirclesHBox.getChildren().addAll(mistakesLabel, circleHBox);

        usedCharsLabel = new Label();
        usedCharsLabel.setId("white-label");

        // Main VBox
        mainVBox = new VBox();
        mainVBox.setSpacing(20);
        mainVBox.setPadding(new Insets(15));
        mainVBox.getChildren().addAll(guessHBox, scrollPane, mistakeCirclesHBox, usedCharsLabel);
    }

    /**
     * @return A VBox with all elements.
     */
    public VBox getPane() {
        return mainVBox;
    }

    /**
     * Returns the text field which only accepts one character.
     * @return The text field.
     */
    public TextField getTextField() {
        return textField;
    }

    /**
     * @return The text from the single char text field.
     */
    public String getTextFieldText() {
        return textField.getText();
    }

    /**
     * Set the text field text to attribute.
     * @param text Is the text to show in the text field.
     */
    public void setTextFieldText(String text) {
        textField.setText(text);
    }

    /**
     * @return The text from the OutputLabel, which is displayed in the scrollPane.
     */
    public String getOutputLabelText() {
        return outputLabel.getText();
    }

    /**
     * Set the text of the outputLabel.
     * @param text The text to show.
     */
    public void setOutputLabel(String text) {
        outputLabel.setText(text);
    }

    /**
     * Get the circleHBox and deletes all children.
     */
    public void clearCircleHBox() {
        circleHBox.getChildren().clear();
    }

    /**
     * Add a circle with a given color to the circleHBox.
     * @param color The color of the circle.
     */
    public void addCircle(Color color) {
        Circle circle = new Circle(0, 0, 5, color);
        circleHBox.getChildren().add(circle);
    }

    /**
     * A list of all used characters is shown in the bottom of the Scene. This method is called to add a new character
     * to it.
     * @param character The character to be added.
     */
    public void addUsedCharacter(char character) {
        usedCharsList.add(character);

        usedCharsLabel.setText("Already used characters: " + usedCharsList.get(0));

        for (int i = 1; i < usedCharsList.size(); i++) {
            usedCharsLabel.setText(usedCharsLabel.getText() + ", " + usedCharsList.get(i));
        }
    }
}