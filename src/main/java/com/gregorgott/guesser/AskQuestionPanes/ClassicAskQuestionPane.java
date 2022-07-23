/*
 * This software is licensed under the GNU GENERAL PUBLIC LICENSE Version 3 from 29 June 2007.
 * This software comes with absolutely no warranty! Please read the LICENSE file.
 */

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
import java.util.Objects;
import java.util.Random;

/**
 * This is the classic game mode where the player tries to guess a word by only knowing the length of the word.
 *
 * @author GregorGott
 * @version 1.2.3
 * @since 2022-07-23
 */
public class ClassicAskQuestionPane extends AskQuestionManager {
    private Node root;
    private HBox guessHBox;
    private TextField textField;
    private Label usedCharactersLabel;
    private final Label outputLabel;

    /**
     * Initialize the <code>outputLabel</code> which later contains the underscores.
     *
     * @since 1.0.0
     */
    public ClassicAskQuestionPane() {
        super();
        outputLabel = new Label();
    }

    /**
     * Sets the <code>root</code> Node to a <code>VBox</code> which contains a <code>ScrollPane</code> with the
     * <code>outputLabel</code>, text field for input, enter and a tip button to show a hint by revealing one
     * character.
     */
    private void setRoot() {
        setOutputLabel();

        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPadding(new Insets(8));
        scrollPane.setPrefHeight(50);
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

        usedCharactersLabel = new Label();
        usedCharactersLabel.setId("white-label");

        VBox vBox = new VBox(scrollPane, guessHBox, getMistakeCirclesNode(), usedCharactersLabel);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));

        root = vBox;
    }

    /**
     * If the input is not empty it checks if the input is in the solution and updates the <code>outputLabel</code>.
     *
     * @param text the text to be checked.
     * @since 1.2.3
     */
    private void checkAndUpdate(String text) {
        if (!Objects.equals(text, "")) {
            check(text.toUpperCase(), false, false);
            setOutputLabel();
            setUsedCharactersNode();
            textField.clear();

            if (isFinished()) {
                guessHBox.setDisable(true);
            }
        }
    }

    /**
     * Update the outputLabel. An underline for each not guessed character.
     */
    private void setOutputLabel() {
        outputLabel.setText(getOutputArrayAsString());
    }

    /**
     * Sets the <code>usedCharactersLabel</code> to the used strings with the <code>getUsedStrings</code> method.
     *
     * @since 1.2.3
     */
    private void setUsedCharactersNode() {
        if (getUsedStrings().size() == 1) {
            usedCharactersLabel.setText("Your last guesses: " + getUsedStrings().get(0));
        } else {
            usedCharactersLabel.setText(usedCharactersLabel.getText() + ", " + getUsedStrings().get(getUsedStrings().size() - 1));
        }
    }

    /**
     * @return the root node with all UI elements.
     * @since 1.2.2
     */
    @Override
    public Node getNode() {
        setRoot();
        return root;
    }

    /**
     * If the player clicks on the tip button, this method gets a random left underscore and replace it with the
     * matching character from the solution.
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

        checkAndUpdate(s);

        setOutputLabel();
        addPoints(-1);
    }
}
