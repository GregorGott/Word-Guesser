package com.gregorgott.guesser.AskQuestionPanes;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The <code>AskQuestionManger</code> is the main class of all question panes. This class controls the check process,
 * points, mistakes and the mistake circles node.
 *
 * @author GregorGott
 * @version 1.0.1
 * @since 2022-07-21
 */
public abstract class AskQuestionManager {
    private final ArrayList<String> usedStrings;
    private final HBox mistakeCirclesHBox;
    private int maxMistakes;
    private int points;
    private int mistakes;
    private char[] outputArray;
    private char[] solutionArray;
    private boolean finished;

    /**
     * Initializes points, mistakes and the finished state with the <code>reset</code> method and initializes a
     * HBox with spacing and center alignment.
     *
     * @since 1.0.0
     */
    public AskQuestionManager() {
        usedStrings = new ArrayList<>();

        reset();

        mistakeCirclesHBox = new HBox();
        mistakeCirclesHBox.setSpacing(8);
        mistakeCirclesHBox.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * @return a node with an ask question pane.
     * @since 1.0.0
     */
    public abstract Node getNode();

    /**
     * @return a node with all mistake circles in a HBox.
     * @since 1.0.0
     */
    public Node getMistakeCirclesNode() {
        setMistakeCirclesNode();

        Label label = new Label("Mistakes:");
        label.setId("white-label");

        HBox hBox = new HBox(label, mistakeCirclesHBox);
        hBox.setSpacing(10);

        return hBox;
    }

    /**
     * The mistake circles are small circles in green or red. Red means mistake, green means "available" mistake.
     * This method creates these circles and loads them with a label in a HBox which can be get by
     * <code>getMistakeCirclesNode</code>.
     *
     * @since 1.0.0
     */
    private void setMistakeCirclesNode() {
        mistakeCirclesHBox.getChildren().clear();

        for (int i = 0; i < mistakes; i++) {
            mistakeCirclesHBox.getChildren().add(getCircle(Color.rgb(170, 12, 12)));
        }

        for (int i = 0; i < maxMistakes - mistakes; i++) {
            mistakeCirclesHBox.getChildren().add(getCircle(Color.rgb(33, 145, 27)));
        }
    }

    /**
     * Returns a circle with the given color.
     *
     * @param color the color of the circle.
     * @since 1.0.0
     */
    private Circle getCircle(Color color) {
        return new Circle(0, 0, 5, color);
    }

    /**
     * @return the <code>outputArray</code>.
     * @since 1.0.0
     */
    public char[] getOutputArray() {
        return outputArray;
    }

    /**
     * Creates an array with underscores for each non whitespace character in the <code>solutionArray</code>.
     *
     * @since 1.0.0
     */
    public void setOutputArray() {
        outputArray = new char[solutionArray.length];

        for (int i = 0; i < solutionArray.length; i++) {
            if (solutionArray[i] == ' ')
                outputArray[i] = ' ';
            else
                outputArray[i] = '_';
        }
    }

    /**
     * @return the reached points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds the given number to the points. It can be also used to subtract points.
     *
     * @param add the number to add to the points
     * @since 1.0.0
     */
    public void addPoints(int add) {
        this.points += add;
    }

    /**
     * Sets the max amount of allowed mistakes.
     *
     * @param maxMistakes the amount of allowed mistakes.
     * @since 1.0.0
     */
    public void setMaxMistakes(int maxMistakes) {
        this.maxMistakes = maxMistakes;
    }

    /**
     * Adds a mistake and check if the max amount of mistakes is reached, if true sets the points to zero, sets finished
     * to true and copies the <code>solutionArray</code> to the <code>outputArray</code> to display the solution.
     *
     * @since 1.0.0
     */
    public void addMistake() {
        this.mistakes++;
        setMistakeCirclesNode();

        if (mistakes == maxMistakes) {
            points = 0;
            finished = true;
            System.arraycopy(solutionArray, 0, outputArray, 0, solutionArray.length);
        }
    }

    /**
     * @return the <code>outputArray</code> as string to show it in a label.
     * @since 1.0.0
     */
    public String getOutputArrayAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : outputArray) {
            stringBuilder.append(c).append(" ");
        }

        return stringBuilder.toString();
    }

    /**
     * @return the <code>solutionArray</code>.
     * @since 1.0.0
     */
    public char[] getSolutionArray() {
        return solutionArray;
    }

    /**
     * Sets the <code>solutionArray</code>.
     *
     * @param solutionArray the word to guess with every character in a new index.
     * @since 1.0.0
     */
    public void setSolutionArray(char[] solutionArray) {
        this.solutionArray = solutionArray;
        setOutputArray();
    }

    /**
     * Checks if input string is in the solution array and adds the input to the <code>outputArray</code>
     * when it's true.
     *
     * @param input the input to be checked.
     * @since 1.0.0
     */
    public boolean check(String input, boolean onlyAddWhenInFrontCharacterIsKnown, boolean acceptUsedInputs) {
        boolean checkCorrect = false;

        if (acceptUsedInputs || !usedStrings.contains(input)) {
            usedStrings.add(input);

            int inputLength = input.length();
            char[] inputAsArray = new char[inputLength];
            ArrayList<Integer> insertIndexes = new ArrayList<>();

            for (int i = 0; i < inputLength; i++) {
                inputAsArray[i] = input.charAt(i);
            }

            for (int i = 0; i < solutionArray.length; i++) {
                ArrayList<Integer> tempInsertIndexes = new ArrayList<>();

                for (int y = 0; y < inputLength; y++) {
                    if (i + y < solutionArray.length) {
                        if (inputAsArray[y] == solutionArray[i + y]) {
                            tempInsertIndexes.add(i + y);
                        } else {
                            break;
                        }
                    }
                }

                if (tempInsertIndexes.size() == inputLength) {
                    insertIndexes.addAll(tempInsertIndexes);
                }
            }

            ArrayList<Character> characters = new ArrayList<>();

            if (onlyAddWhenInFrontCharacterIsKnown) {
                if (insertIndexes.get(0) == 0 || outputArray[insertIndexes.get(0) - 1] != '_') {
                    for (int i : insertIndexes) {
                        outputArray[i] = solutionArray[i];
                        characters.add(solutionArray[i]);
                    }

                    addPoints(calculatePoints(characters));
                    checkCorrect = true;
                } else {
                    addMistake();
                }
            } else {
                if (insertIndexes.size() == 0) {
                    addMistake();
                } else {
                    for (int i : insertIndexes) {
                        outputArray[i] = solutionArray[i];
                        characters.add(solutionArray[i]);
                    }

                    addPoints(calculatePoints(characters));
                    checkCorrect = true;
                }
            }

            if (Arrays.equals(solutionArray, outputArray)) {
                finished = true;
            }
        }

        return checkCorrect;
    }

    /**
     * Calculates the points for the given characters. Note that one character doesn't count twice.
     *
     * @param characters an <code>ArrayList</code> with the checked input.
     * @return the calculated points.
     * @since 1.0.1
     */
    private int calculatePoints(ArrayList<Character> characters) {
        ArrayList<Character> tempArrayList = new ArrayList<>();

        int y = 0;

        for (int i = 0; i < characters.size(); i++) {
            char c = characters.get(i);
            if (!tempArrayList.contains(c) && !usedStrings.contains(String.valueOf(c))) {
                tempArrayList.add(c);
                characters.add(c);
                y++;
            }
        }

        return y;
    }

    /**
     * @return a boolean if the round is finished.
     * @since 1.0.0
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Resets the variables <code>points</code>, <code>mistakes</code>, <code>finished</code> and clears the
     * <code>usedStrings</code> ArrayList.
     *
     * @since 1.0.1
     */
    public void reset() {
        points = 0;
        mistakes = 0;
        finished = false;

        usedStrings.clear();
    }
}
