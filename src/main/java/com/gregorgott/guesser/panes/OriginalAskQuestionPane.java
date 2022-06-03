package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
 * This is the classic game mode where the player tries to guess a word by only knowing the length of the word.
 *
 * @author GregorGott
 * @version 1.1.5
 * @since 2022-06-03
 */
public class OriginalAskQuestionPane {
    private final char[] solutionArray;
    private final char[] outputArray;
    private final int maxMistakes;
    private final List<Character> usedCharsList;
    private final Node root;
    private HBox circleHBox, hBox_1;
    private Label outputLabel;
    private Label usedCharsLabel;
    private TextField textField;
    private Button showTipButton;
    private int points, mistakes;
    private boolean finished;

    /**
     * Initialize variables, set points and mistakes to zero and set finished to false. Fill all non-whitespace characters
     * with underscores.
     *
     * @param solutionArray an Array with the solution.
     * @param maxMistakes   Max amount of mistakes. Each mistake is one circle.
     * @see <a href="https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength">JavaFX 2.2 TextField maxlength</a>
     */
    public OriginalAskQuestionPane(char[] solutionArray, int maxMistakes) {
        this.maxMistakes = maxMistakes;
        this.solutionArray = solutionArray;

        outputArray = new char[solutionArray.length];

        for (int i = 0; i < this.solutionArray.length; i++) {
            if (this.solutionArray[i] == ' ') {
                outputArray[i] = ' ';
            } else {
                outputArray[i] = '_';
            }
        }

        usedCharsList = new ArrayList<>();

        points = 0;
        mistakes = 0;
        finished = false;

        root = loadAskQuestionPane();
    }

    /**
     * @return The root node with all UI elements.
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Show a text field which only accepts single characters, a button to check if the input is in the word and a
     * button to show a tip. The output is shown in a scroll pane. Underneath is the mistakes counter and
     * a list with all used characters.
     *
     * @return A node with all UI elements.
     */
    private Node loadAskQuestionPane() {
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

        showTipButton = new Button("Show Tip");
        setShowTipButton();

        // textField, checkGuessButton and tipButton are in a HBox
        hBox_1 = new HBox();
        hBox_1.setAlignment(Pos.CENTER_LEFT);
        hBox_1.setSpacing(10);
        hBox_1.getChildren().addAll(enterACharLabel, textField, checkGuessButton, showTipButton);

        // Underline for each word in "solutionArray"
        outputLabel = new Label();
        setOutputLabel();

        // ScrollPane to scroll if the word is too long
        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setPrefHeight(50);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPadding(new Insets(8));

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
     * Update the outputLabel. An underline for each not guessed character.
     */
    private void setOutputLabel() {
        outputLabel.setText("");
        for (char c : outputArray) {
            outputLabel.setText(outputLabel.getText() + " " + c + " ");
        }
    }

    /**
     * Get the textField text and check if it is empty. If not convert it to uppercase and check if it has been entered
     * before. If not, check if the character in the solutionLabelArray, when it is, add one point and show the
     * character in the outputLabelArray.
     */
    private void checkGuess() {
        // Get input
        String s = textField.getText();
        textField.setText("");

        if (!s.isEmpty()) {
            // Convert input to uppercase
            char inputInUppercase = s.toUpperCase().charAt(0);

            // Check if the character has been entered before
            if (!usedCharsList.contains(inputInUppercase)) {
                addUsedCharacter(inputInUppercase);

                // Check if the character is in the array
                if (isCharInArray(inputInUppercase, solutionArray)) {
                    points++;

                    // Replace underlines with every character from input
                    for (int i = 0; i < solutionArray.length; i++) {
                        if (solutionArray[i] == inputInUppercase) {
                            outputArray[i] = solutionArray[i];
                            setOutputLabel();

                            if (Arrays.equals(solutionArray, outputArray)) {
                                endRound();
                            }
                        }
                    }
                } else {
                    // Input not in array
                    mistakes++;
                    setCircleHBox();

                    if (mistakes == maxMistakes) {
                        // Delete all points and end the round
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
     * A list of all used characters is shown at the bottom of the Scene. This method is called adding a new character
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
     * Disable the input text field and show the solution if not guessed.
     */
    private void endRound() {
        hBox_1.setDisable(true);
        finished = true;

        System.arraycopy(solutionArray, 0, outputArray, 0, solutionArray.length);
        usedCharsList.clear();

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
     * @return A boolean if the round is ended.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * If the player clicks on this button, get a random left underline and replace it with the matching character from
     * the solutionArray.
     */
    private void setShowTipButton() {
        showTipButton.setOnAction(event -> {
            // Get index from every underscore in outputArray
            List<Integer> leftUnderlinesIndex = new ArrayList<>();

            // Count number of underscores in output array
            for (int i = 0; i < outputArray.length; i++) {
                if (outputArray[i] == '_') {
                    leftUnderlinesIndex.add(i);
                }
            }

            // Get random index from the array list and get the character in the solution array
            int random = new Random().nextInt(leftUnderlinesIndex.size());
            char c = solutionArray[leftUnderlinesIndex.get(random)];

            // Show the tip in output array by replacing every c in outputArray
            for (int i = 0; i < solutionArray.length; i++) {
                if (solutionArray[i] == c) {
                    outputArray[i] = solutionArray[i];
                }
            }

            setOutputLabel();
            addUsedCharacter(c);
            points--;

            if (Arrays.equals(solutionArray, outputArray)) {
                endRound();
            }
        });
    }

    /**
     * Get the circleHBox and deletes all children.
     */
    public void clearCircleHBox() {
        circleHBox.getChildren().clear();
    }

    /**
     * Add a circle with a given colour to the circleHBox.
     *
     * @param color The colour of the circle.
     */
    public void addCircle(Color color) {
        Circle circle = new Circle(0, 0, 5, color);
        circleHBox.getChildren().add(circle);
    }
}