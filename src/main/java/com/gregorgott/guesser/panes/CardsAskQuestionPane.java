package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The cards question mode is simpler than the classic mode, because each character is shown in a button.
 *
 * @author GregorGott
 * @version 1.1.2
 * @since 2022-06-14
 */
public class CardsAskQuestionPane extends AskQuestionPane {
    private final Label outputLabel;
    private final ArrayList<Character> sortedArray;
    private final ArrayList<Button> buttons;
    private final char[] solutionArray;
    private final char[] outputArray;
    private final int maxMistakes;
    private Node root;
    private boolean finished;

    /**
     * The constructor initializes needed variables and fills the solution array with underscores for each character.
     *
     * @param solutionArray the solution as an array to check the players input.
     * @since 1.0.0
     */
    public CardsAskQuestionPane(char[] solutionArray, int maxMistakes) {
        super(maxMistakes);
        this.solutionArray = solutionArray;
        this.maxMistakes = maxMistakes;
        outputLabel = new Label();

        outputArray = createOutputArray(solutionArray);
        sortedArray = new ArrayList<>();
        buttons = new ArrayList<>();

        finished = false;

        setSortedArray();
        setRoot();
    }

    /**
     * @return The root Pane with all elements.
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Sets the root pane with a scroll pane which includes the output array and a flow pane with the character
     * buttons.
     * All these elements are children of a v box.
     *
     * @since 1.0.0
     */
    private void setRoot() {
        setOutputLabel();

        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefHeight(50);
        scrollPane.setPadding(new Insets(8));

        addCharacterButtons();

        FlowPane characterButtonsFlowPane = new FlowPane();
        characterButtonsFlowPane.setHgap(10);
        characterButtonsFlowPane.setVgap(10);
        characterButtonsFlowPane.setAlignment(Pos.CENTER);
        characterButtonsFlowPane.getChildren().addAll(buttons);

        VBox vBox = new VBox(scrollPane, characterButtonsFlowPane, getCirclesPane());
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));
        root = vBox;
    }

    /**
     * Creates a button for each character in the <code>sortedArray</code> with an ActionEvent.
     *
     * @since 2022-06-03
     */
    private void addCharacterButtons() {
        while (sortedArray.size() > 0) {
            String s = String.valueOf(getRandomFromArray(sortedArray));

            Button button = new Button(s);
            button.setOnAction(x -> checkButton(button, s.charAt(0)));
            sortedArray.remove(Character.valueOf(s.charAt(0)));
            buttons.add(button);
        }
    }

    /**
     * This method is called when a character button is pushed. It checks if the pushed character is the first character
     * or the character before is already pushed.
     *
     * @param c the button text as a char.
     * @since 2022-06-03
     */
    private void checkButton(Button button, char c) {
        int firstPosition = 0;

        // Get the first position
        for (int i = 0; i < solutionArray.length; i++) {
            if (solutionArray[i] == c) {
                firstPosition = i;
                break;
            }
        }

        // If the char is the first one in the solutionArray
        if (firstPosition == 0 || outputArray[firstPosition - 1] != '_') {
            addPoints(1);
            for (int i = 0; i < solutionArray.length; i++) {
                if (solutionArray[i] == c) {
                    outputArray[i] = solutionArray[i];
                }
            }

            button.setDisable(true);
        } else {
            addMistake();
        }

        if (maxMistakes == getMistakes()) {
            setPoints(0);
            endRound();
        } else {
            setOutputLabel();

            // Check if the solution is found
            if (Arrays.equals(solutionArray, outputArray)) {
                endRound();
            }
        }
    }

    /**
     * Creates an array list, which only contains each character in the solution array once.
     *
     * @since 2022-06-03
     */
    private void setSortedArray() {
        for (char c : solutionArray) {
            if (!sortedArray.contains(c)) {
                sortedArray.add(c);
            }
        }
    }

    /**
     * Updates the <code>outputLabel</code> by copying the <code>outputArray</code>.
     *
     * @since 1.0.0
     */
    private void setOutputLabel() {
        outputLabel.setText("");
        for (char c : outputArray) {
            outputLabel.setText(outputLabel.getText() + c + " ");
        }
    }

    /**
     * @param array the array from which a random element is to be selected.
     * @return a random char from the array.
     */
    private char getRandomFromArray(ArrayList<Character> array) {
        int random = new Random().nextInt(array.size());

        return array.get(random);
    }

    private void endRound() {
        finished = true;

        for (Button button : buttons) {
            button.setDisable(true);
        }
    }

    /**
     * @return a boolean if the round is finished
     * @since 1.0.0
     */
    public boolean isFinished() {
        return finished;
    }
}