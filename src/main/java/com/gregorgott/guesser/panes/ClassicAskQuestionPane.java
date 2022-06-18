package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This is the classic game mode where the player tries to guess a word by only knowing the length of the word.
 *
 * @author GregorGott
 * @version 1.2.1
 * @since 2022-06-14
 */
public class ClassicAskQuestionPane extends AskQuestionPane {
    private final char[] solutionArray;
    private final char[] outputArray;
    private final int maxMistakes;
    private Node root;
    private HBox enterCharHBox;
    private Label outputLabel;
    private Label usedCharsLabel;
    private TextField textField;
    private Button showTipButton;
    private boolean finished;

    /**
     * Initialize variables, set points and mistakes to zero and set finished to false. Fill all non-whitespace characters
     * with underscores.
     *
     * @param solutionArray an Array with the solution.
     * @param maxMistakes   Max amount of mistakes. Each mistake is one circle.
     * @see <a href="https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength">JavaFX 2.2 TextField maxlength</a>
     */
    public ClassicAskQuestionPane(char[] solutionArray, int maxMistakes) {
        super(maxMistakes);
        this.maxMistakes = maxMistakes;
        this.solutionArray = solutionArray;

        outputArray = createOutputArray(solutionArray);

        finished = false;

        setRoot();
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
     */
    private void setRoot() {
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
        enterCharHBox = new HBox(enterACharLabel, textField, checkGuessButton, showTipButton);
        enterCharHBox.setSpacing(10);

        // Underline for each word in "solutionArray"
        outputLabel = new Label();
        setOutputLabel();

        // ScrollPane to scroll if the word is too long
        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setPrefHeight(50);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPadding(new Insets(8));

        usedCharsLabel = new Label();
        usedCharsLabel.setId("white-label");

        // Main VBox
        VBox mainVBox = new VBox(enterCharHBox, scrollPane, getCirclesPane(), usedCharsLabel);
        mainVBox.setSpacing(20);
        mainVBox.setPadding(new Insets(20));

        root = mainVBox;
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
     * before. If not, check if the character is in the <code>solutionLabelArray</code>, when it is, add one point and show the
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
            if (!getUsedCharsList().contains(inputInUppercase)) {
                updateUsedCharsLabel(inputInUppercase);

                // Check if the character is in the array
                if (isCharInArray(inputInUppercase, solutionArray)) {
                    addPoints(1);
                    updateOutputArray(inputInUppercase);
                } else {
                    // Input not in array
                    addMistake();
                }
            }

            if (getMistakes() == maxMistakes) {
                // Delete all points and end the round
                setPoints(0);
                endRound();
            } else {
                setOutputLabel();

                if (Arrays.equals(solutionArray, outputArray)) {
                    endRound();
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
    private void updateUsedCharsLabel(char character) {
        getUsedCharsList().add(character);

        if (getUsedCharsList().size() == 1) {
            usedCharsLabel.setText("Used characters: " + getUsedCharsList().get(0));
        } else {
            usedCharsLabel.setText(usedCharsLabel.getText() + ", " + super.getUsedCharsList().get(getUsedCharsList().size() - 1));
        }
    }

    /**
     * Disable the input text field and show the solution if not guessed.
     */
    private void endRound() {
        enterCharHBox.setDisable(true);
        finished = true;

        System.arraycopy(solutionArray, 0, outputArray, 0, solutionArray.length);
        getUsedCharsList().clear();

        setOutputLabel();
    }

    /**
     * @return A boolean if the round is ended.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Places every given <code>c</code> in the <code>solutionArray</code> in the <code>outputArray</code>.
     *
     * @param c the character that should be placed in the <code>outputArray</code>.
     * @since 1.2.1
     */
    private void updateOutputArray(char c) {
        for (int i = 0; i < solutionArray.length; i++) {
            if (solutionArray[i] == c) {
                outputArray[i] = solutionArray[i];
            }
        }
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
            updateOutputArray(c);

            setOutputLabel();
            updateUsedCharsLabel(c);
            addPoints(-1);

            if (Arrays.equals(solutionArray, outputArray)) {
                endRound();
            }
        });
    }
}