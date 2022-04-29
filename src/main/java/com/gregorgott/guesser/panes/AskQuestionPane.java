package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Show a pane where the player tries to guess the given word. And control the mistakes circles. Each Mistake is one
 * circle.
 *
 * @author GregorGott
 * @version 1.1.0
 * @since 2022-04-29
 */
public class AskQuestionPane {
    private final char[] solutionLabelArray, outputLabelArray;
    private final int maxMistakes;
    private Node root;
    private HBox circleHBox, hBox_1;
    private List<Character> usedCharsList;
    private Label outputLabel, usedCharsLabel;
    private TextField textField;

    private int points, mistakes;
    private boolean finished;

    /**
     * Initialize variables, set points and mistakes to zero and set finished to false.
     *
     * @param solutionLabelArray Each character is an underscore.
     * @param maxMistakes        Max amount of mistakes. Each mistake is one circle.
     * @see <a href="https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength">JavaFX 2.2 TextField maxlength</a>
     */
    public AskQuestionPane(char[] solutionLabelArray, int maxMistakes) {
        this.maxMistakes = maxMistakes;
        this.solutionLabelArray = solutionLabelArray;

        outputLabelArray = new char[solutionLabelArray.length];
        Arrays.fill(outputLabelArray, '_');

        usedCharsList = new ArrayList<>();

        points = 0;
        mistakes = 0;
        finished = false;

        setRoot(loadAskQuestionPane());
    }

    /**
     * @return The root pane with all UI elements.
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Set the root node.
     * @param parent Is the node to set root to.
     */
    private void setRoot(Node parent) {
        root = parent;
    }

    /**
     * Show a scroll pane with underscores for each char in solutionArray. The player tries to guess the word with
     * the textField.
     * @return All ui elements.
     */
    private Node loadAskQuestionPane() {
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
        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                checkGuess();
            }
        });
        HBox.setHgrow(textField, Priority.ALWAYS);

        // Check button
        Button checkGuessButton = new Button("Check Guess");
        checkGuessButton.setOnAction(event -> checkGuess());

        // textField and checkGuessButton are in a guessHBox
        hBox_1 = new HBox();
        hBox_1.setAlignment(Pos.CENTER_LEFT);
        hBox_1.setSpacing(10);
        hBox_1.getChildren().addAll(enterACharLabel, textField, checkGuessButton);

        setShowTipButton();

        // Underline/Underscore for each word in "solutionArray"
        outputLabel = new Label();
        setOutputLabel();

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

        setCircleHBox();

        // HBox with enterACharLabel and mistake circles
        HBox mistakeCirclesHBox = new HBox();
        mistakeCirclesHBox.setSpacing(10);
        mistakeCirclesHBox.getChildren().addAll(mistakesLabel, circleHBox);

        usedCharsLabel = new Label();
        usedCharsLabel.setId("white-label");

        // Main VBox
        VBox mainVBox = new VBox();
        mainVBox.setSpacing(20);
        mainVBox.setPadding(new Insets(20));
        mainVBox.getChildren().addAll(hBox_1, scrollPane, mistakeCirclesHBox, usedCharsLabel);

        return mainVBox;
    }

    /**
     * Update the outputLabel. An underscore for each not guessed character.
     */
    private void setOutputLabel() {
        outputLabel.setText("");
        for (char c : outputLabelArray) {
            outputLabel.setText(outputLabel.getText() + " " + c + " ");
        }
    }

    /**
     * Get the textField text and check if it is empty. If not convert it to uppercase and check if it has been entered
     * before. If not check, is the character in the solutionLabelArray, when it is, add one point and show the
     * character in the outputLabelArray.
     */
    private void checkGuess() {
        String s = textField.getText();
        textField.setText("");

        if (!s.isEmpty()) {
            // Convert input to uppercase
            char inputInUppercase = s.toUpperCase().charAt(0);

            // Check if the character has been entered before
            if (!usedCharsList.contains(inputInUppercase)) {
                addUsedCharacter(inputInUppercase);

                // Check if the character is in the array
                if (isCharInArray(inputInUppercase, solutionLabelArray)) {
                    points++;

                    // Replace underscores with every character from input
                    for (int i = 0; i < solutionLabelArray.length; i++) {
                        if (solutionLabelArray[i] == inputInUppercase) {
                            outputLabelArray[i] = solutionLabelArray[i];
                            setOutputLabel();

                            if (Arrays.equals(solutionLabelArray, outputLabelArray)) {
                                endRound();
                            }
                        }
                    }
                } else {
                    // Input not in array
                    mistakes++;
                    setCircleHBox();

                    if (mistakes == maxMistakes) {
                        points = 0;
                        endRound();
                    }
                }
            }
        }
    }

    /**
     * Check if the value is in the chars array.
     *
     * @param value This char is checked.
     * @param chars The value is searched in this array.
     * @return A boolean if the value is in the array.
     */
    private boolean isCharInArray(char value, char[] chars) {
        for (char c : chars) {
            if (c == value) {
                return true;
            }
        }
        return false;
    }


    /**
     * A list of all used characters is shown in the bottom of the Scene. This method is called to add a new character
     * to it.
     *
     * @param character The character to be added.
     */
    private void addUsedCharacter(char character) {
        usedCharsList.add(character);

        usedCharsLabel.setText("Already used characters: " + usedCharsList.get(0));

        for (int i = 1; i < usedCharsList.size(); i++) {
            usedCharsLabel.setText(usedCharsLabel.getText() + ", " + usedCharsList.get(i));
        }
    }

    /**
     * Disable input and show the solution if not guessed.
     */
    private void endRound() {
        hBox_1.setDisable(true);
        finished = true;

        System.arraycopy(solutionLabelArray, 0, outputLabelArray, 0, solutionLabelArray.length);

        setOutputLabel();
    }

    /**
     * For each mistake a red and for each left mistake a green circle.
     */
    private void setCircleHBox() {
        clearCircleHBox();

        for (int i = 0; i < mistakes; i++) {
            addCircle(Color.rgb(170, 12, 12));
        }

        if (mistakes < maxMistakes) {
            for (int i = 0; i < maxMistakes - mistakes; i++) {
                addCircle(Color.rgb(33, 145, 27));
            }
        }
    }

    /**
     * @return The points the player achieved.
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return A boolean if the player guessed the word.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * If the player clicks on this button, get a random left underscore and replace it with the matching character from
     * the solutionArray.
     */
    private void setShowTipButton() {
        Button showTipButton = new Button("Show Tip");
        showTipButton.setOnAction(event -> {
            ArrayList<Integer> leftUnderscoresIndex = new ArrayList<>();

            for (int i = 0; i < outputLabelArray.length; i++) {
                if (outputLabelArray[i] == '_') {
                    leftUnderscoresIndex.add(i);
                }
            }

            int random = new Random().nextInt(leftUnderscoresIndex.size());
            char c = solutionLabelArray[leftUnderscoresIndex.get(random)];

            for (int i = 0; i < solutionLabelArray.length; i++) {
                if (solutionLabelArray[i] == c) {
                    outputLabelArray[i] = solutionLabelArray[i];
                    setOutputLabel();
                }
            }

            addUsedCharacter(c);
            points--;

            if (Arrays.equals(solutionLabelArray, outputLabelArray)) {
                endRound();
            }
        });

        hBox_1.getChildren().add(showTipButton);
    }

    /**
     * Get the circleHBox and deletes all children.
     */
    public void clearCircleHBox() {
        circleHBox.getChildren().clear();
    }

    /**
     * Add a circle with a given color to the circleHBox.
     *
     * @param color The color of the circle.
     */
    public void addCircle(Color color) {
        Circle circle = new Circle(0, 0, 5, color);
        circleHBox.getChildren().add(circle);
    }
}