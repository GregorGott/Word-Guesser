package com.gregorgott.guesser;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
 * @version 1.0.0
 * @since 2022-04-05
 */
public class AskQuestionPane {
    public Button checkGuessButton;
    public Label outputLabel, usedCharsLabel;
    public TextField textField;
    private final VBox mainVBox;
    private final HBox circleHBox;

    private final List<Character> usedCharsList;

    /**
     * Set all elements for the UI. Create a text field in which single letters are written to guess the word.
     * Each character in the outputLabelArray is one underscore in the scroll pane.
     * @param   outputLabelArray    Each character is an underscore.
     * @param   maxMistakes         Max amount of mistakes. Each mistake is one circle.
     */
    public AskQuestionPane(char[] outputLabelArray, int maxMistakes) {
        Label enterACharLabel = new Label("Enter a character:");

        textField = new TextField();
        // The listener avoids entering more than one character
        textField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (textField.getText().length() > 1) {
                String s = textField.getText().substring(0, 1);
                textField.setText(s);
            }
        });

        // Check button
        checkGuessButton = new Button("Check Guess");

        // textField and checkGuessButton are in a guessHBox
        HBox guessHBox = new HBox();
        guessHBox.setSpacing(20);
        guessHBox.getChildren().addAll(textField, checkGuessButton);

        // Underline/Underscore for each word in "solutionArray"
        outputLabel = new Label();
        for (char c : outputLabelArray) {
            outputLabel.setText(outputLabel.getText() + " " + c + " ");
        }

        // ScrollPane to scroll if the word is too long
        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setPrefHeight(50);
        scrollPane.setFocusTraversable(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPadding(new Insets(10, 10, 10, 10));

        Label mistakeHeadline = new Label("Mistakes:");

        // HBox which contains mistake circles
        circleHBox = new HBox();
        circleHBox.setSpacing(8);

        // Create circle for each available mistake
        for (int i = 0; i < maxMistakes; i++) {
            addCircle(Color.rgb(27, 94, 23));
        }

        // HBox with enterACharLabel and mistake circles
        HBox mistakeCirclesHBox = new HBox();
        mistakeCirclesHBox.setSpacing(25);
        mistakeCirclesHBox.getChildren().addAll(mistakeHeadline, circleHBox);

        usedCharsLabel = new Label();
        usedCharsList = new ArrayList<>();

        VBox pointsInfoVBox = new VBox();
        pointsInfoVBox.setPadding(new Insets(18, 8, 8, 8));
        pointsInfoVBox.setSpacing(10);
        pointsInfoVBox.getChildren().addAll(mistakeCirclesHBox, usedCharsLabel);

        // Main VBox
        mainVBox = new VBox();
        mainVBox.setSpacing(10);
        mainVBox.setPadding(new Insets(15, 15, 15, 15));
        mainVBox.getChildren().addAll(enterACharLabel, guessHBox, scrollPane, pointsInfoVBox);
    }

    /**
     * @return A VBox with all elements.
     */
    public VBox getPane() {
        return mainVBox;
    }

    /**
     * @return The text from the single char text field.
     */
    public String getTextField() {
        return this.textField.getText();
    }

    public void clearCircleHBox() {
        circleHBox.getChildren().clear();
    }

    public void addCircle(Color color) {
        Circle circle = new Circle(0, 0, 5, color);
        circleHBox.getChildren().add(circle);
    }

    public void addUsedCharacter(char character) {
        usedCharsList.add(character);

        usedCharsLabel.setText("Already used characters: " + usedCharsList.get(0));

        for (int i = 1; i < usedCharsList.size(); i++) {
            usedCharsLabel.setText(usedCharsLabel.getText() + ", " + usedCharsList.get(i));
        }
    }
}