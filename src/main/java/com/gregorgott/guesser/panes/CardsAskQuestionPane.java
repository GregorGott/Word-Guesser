package com.gregorgott.guesser.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The cards question mode is simpler than the original, because each character is shown in a button.
 *
 * @author GregorGott
 * @version 1.0.0
 * @since 2022-06-03
 */
public class CardsAskQuestionPane {
    private final char[] solutionArray;
    private final char[] outputArray;
    private final ArrayList<Character> sortedArray;
    private final Pane root;
    private final Label outputLabel;
    private final ArrayList<Button> buttons;
    private int mistakes;
    private boolean finished;
    private int points;

    /**
     * The constructor initializes needed variables and fills the solution array with underscores for each character.
     *
     * @param solutionArray the solution as an array to check the players input.
     * @since 1.0.0
     */
    public CardsAskQuestionPane(char[] solutionArray) {
        this.solutionArray = solutionArray;
        outputArray = new char[solutionArray.length];
        sortedArray = createSortedArray();
        buttons = new ArrayList<>();

        for (int i = 0; i < solutionArray.length; i++) {
            if (solutionArray[i] == ' ') {
                outputArray[i] = ' ';
            } else {
                outputArray[i] = '_';
            }
        }

        points = 0;
        mistakes = 0;
        finished = false;

        root = new Pane();
        outputLabel = new Label();

        setRoot();
    }

    /**
     * @return The root Pane with all elements.
     */
    public Pane getRoot() {
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
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefWidth(200);
        scrollPane.setPrefHeight(50);
        scrollPane.setPadding(new Insets(8));

        createCharacterButtons();

        FlowPane characterButtonsFlowPane = new FlowPane();
        characterButtonsFlowPane.setHgap(10);
        characterButtonsFlowPane.setVgap(10);
        characterButtonsFlowPane.setAlignment(Pos.CENTER);
        characterButtonsFlowPane.getChildren().addAll(buttons);

        VBox vBox = new VBox(scrollPane, characterButtonsFlowPane);
        vBox.setSpacing(20);
        root.getChildren().add(vBox);
    }

    /**
     * Creates a button for each character in the <code>sortedArray</code> with an ActionEvent.
     *
     * @since 2022-06-03
     */
    private void createCharacterButtons() {
        while (sortedArray.size() > 0) {
            String s = String.valueOf(getRandomFromArray(sortedArray));

            Button button = new Button(s);
            button.setOnAction(x -> checkButton(s.charAt(0)));
            sortedArray.remove(new Character(s.charAt(0)));
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
    private void checkButton(char c) {
        if (checkIfArrayContains(solutionArray, c)) {
            int firstPosition = 0;

            // Get the first position
            for (int i = 0; i < solutionArray.length; i++) {
                if (solutionArray[i] == c) {
                    firstPosition = i;
                    break;
                }
            }

            // If the char is the first one in the solutionArray
            if (firstPosition == 0) {
                for (int i = 0; i < solutionArray.length; i++) {
                    if (solutionArray[i] == c) {
                        outputArray[i] = solutionArray[i];
                    }
                }
            } else {
                if (outputArray[firstPosition - 1] != '_') {
                    for (int i = 0; i < solutionArray.length; i++) {
                        if (solutionArray[i] == c) {
                            outputArray[i] = solutionArray[i];
                        }
                    }
                }
            }

            setOutputLabel();
            points++;

            // If the solution is found
            if (Arrays.equals(solutionArray, outputArray)) {
                finished = true;
            }
        } else {
            mistakes++;
        }
    }

    /**
     * This Method checks if the given array contains a key as a char.
     *
     * @param array the array.
     * @param key   the key that is searched for in the array.
     * @return a boolean, if the key is in the array.
     * @since 2022-06-03
     */
    private boolean checkIfArrayContains(char[] array, char key) {
        for (char a : array) {
            if (a == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates an array list, which only contains each character in the solution array once.
     *
     * @return a sorted array list.
     * @since 2022-06-03
     */
    private ArrayList<Character> createSortedArray() {
        ArrayList<Character> tempList = new ArrayList<>();

        for (char c : solutionArray) {
            if (!tempList.contains(c)) {
                tempList.add(c);
            }
        }

        return tempList;
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

    /**
     * @return a boolean if the round is finished
     * @since 1.0.0
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * @return the reached points.
     * @since 1.0.0
     */
    public int getPoints() {
        return points;
    }
}