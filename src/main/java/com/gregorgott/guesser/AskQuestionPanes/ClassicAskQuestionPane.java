package com.gregorgott.guesser.AskQuestionPanes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is the classic game mode where the player tries to guess a word by only knowing the length of the word.
 *
 * @author GregorGott
 * @version 1.2.1
 * @since 2022-06-14
 */
public class ClassicAskQuestionPane extends AskQuestionManager {
    private Node root;
    private HBox guessHBox;
    private TextField textField;
    private final Label outputLabel;
    private boolean finished;

    /**
     * Initialize variables, set points and mistakes to zero and set finished to false. Fill all non-whitespace characters
     * with underscores.
     */
    public ClassicAskQuestionPane() {
        super();
        outputLabel = new Label();

        finished = false;
    }

    /**
     * Show a text field which only accepts single characters, a button to check if the input is in the word and a
     * button to show a tip. The output is shown in a scroll pane. Underneath is the mistakes counter and
     * a list with all used characters.
     */
    private void setRoot() {
        setOutputLabel();

        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setPrefHeight(35);
        scrollPane.setPadding(new Insets(2));
        scrollPane.setPrefWidth(Integer.MAX_VALUE);

        Label guessLabel = new Label("Guess:");
        guessLabel.setId("white-label");

        textField = new TextField();
        textField.setOnKeyReleased(x -> {
            if (x.getCode() == KeyCode.ENTER) {
                checkAndUpdate(textField.getText().toUpperCase());
            }
        });
        textField.setMinWidth(120);

        Button checkButton = new Button("Check");
        checkButton.setOnAction(x -> checkAndUpdate(textField.getText().toUpperCase()));

        Button tipButton = new Button("Show Tip");
        tipButton.setOnAction(x -> showTip());

        tipButton.setMinWidth(80);

        guessHBox = new HBox(guessLabel, textField, checkButton, tipButton);
        guessHBox.setSpacing(10);
        guessHBox.setAlignment(Pos.CENTER_LEFT);

        VBox vBox = new VBox(scrollPane, guessHBox, getMistakeCirclesNode());
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));

        root = vBox;
    }

    private void checkAndUpdate(String text) {
        check(text.toUpperCase(), false);
        setOutputLabel();
        textField.clear();
    }

    /**
     * Update the outputLabel. An underline for each not guessed character.
     */
    private void setOutputLabel() {
        outputLabel.setText(getOutputArrayAsString());
    }


//    /**
//     * A list of all used characters is shown at the bottom of the Scene. This method is called adding a new character
//     * to it.
//     *
//     * @param character The character to be added.
//     */
//    private void updateUsedCharsLabel(char character) {
//        getUsedCharsList().add(character);
//
//        if (getUsedCharsList().size() == 1) {
//            usedCharsLabel.setText("Used characters: " + getUsedCharsList().get(0));
//        } else {
//            usedCharsLabel.setText(usedCharsLabel.getText() + ", " + super.getUsedCharsList().get(getUsedCharsList().size() - 1));
//        }
//    }

    /**
     * Disable the input text field and show the solution if not guessed.
     */
    private void endRound() {
        guessHBox.setDisable(true);
        finished = true;

        setOutputLabel();
    }


    /**
     * @return The root node with all UI elements.
     */
    @Override
    public Node getNode() {
        setRoot();
        return root;
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
    private void showTip() {
        // Get index from every underscore in outputArray
        List<Integer> leftUnderlinesIndex = new ArrayList<>();

        // Count number of underscores in output array
        for (int i = 0; i < getOutputArray().length; i++) {
            if (getOutputArray()[i] == '_') {
                leftUnderlinesIndex.add(i);
            }
        }

        // Get random index from the array list and get the character in the solution array
        int random = new Random().nextInt(leftUnderlinesIndex.size());
        String s = String.valueOf(getSolutionArray()[leftUnderlinesIndex.get(random)]);

        // Show the tip in output array by replacing every c in outputArray
        check(s, false);

        setOutputLabel();
        addPoints(-1);
    }
}
