package com.gregorgott.guesser.AskQuestionPanes;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public abstract class AskQuestionManager {
    private final HBox mistakeCirclesHBox;
    private int maxMistakes;
    private int points;
    private int mistakes;
    private char[] outputArray;
    private char[] solutionArray;
    private boolean finished;

    public AskQuestionManager() {
        this.points = 0;
        this.mistakes = 0;

        mistakeCirclesHBox = new HBox();
        mistakeCirclesHBox.setSpacing(10);
        mistakeCirclesHBox.setAlignment(Pos.CENTER_LEFT);
    }

    public abstract Node getNode();

    public Node getMistakeCirclesNode() {
        setMistakeCircleNode();
        return mistakeCirclesHBox;
    }

    public void setMistakeCircleNode() {
        Label label = new Label("Mistakes:");
        label.setId("white-label");

        mistakeCirclesHBox.getChildren().clear();
        mistakeCirclesHBox.getChildren().add(label);

        for (int i = 0; i < mistakes; i++) {
            mistakeCirclesHBox.getChildren().add(getCircle(Color.rgb(170, 12, 12)));
        }

        if (mistakes < maxMistakes) {
            for (int i = 0; i < maxMistakes - mistakes; i++) {
                mistakeCirclesHBox.getChildren().add(getCircle(Color.rgb(33, 145, 27)));
            }
        }
    }

    /**
     * Add a circle with a given colour to the circleHBox.
     *
     * @param color The colour of the circle.
     * @since 1.0.0
     */
    private Circle getCircle(Color color) {
        return new Circle(0, 0, 5, color);
    }

    public char[] getOutputArray() {
        return outputArray;
    }

    public void setOutputArray() {
        outputArray = new char[solutionArray.length];

        for (int i = 0; i < solutionArray.length; i++) {
            if (solutionArray[i] == ' ')
                outputArray[i] = ' ';
            else
                outputArray[i] = '_';
        }
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int add) {
        this.points += add;
    }

    public void setMaxMistakes(int maxMistakes) {
        this.maxMistakes = maxMistakes;
    }

    public int getMistakes() {
        return mistakes;
    }

    // addMistake ersetze durch getMistake()++ ??
    public void addMistake() {
        this.mistakes++;
    }

    public String getOutputArrayAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : outputArray) {
            stringBuilder.append(c).append(" ");
        }

        return stringBuilder.toString();
    }

    public char[] getSolutionArray() {
        return solutionArray;
    }

    public void setSolutionArray(char[] solutionArray) {
        this.solutionArray = solutionArray;
        setOutputArray();
    }

    /**
     * Checks if input string is in the solution array and adds the input to the <code>outputArray</code>
     * when it's true.
     *
     * @param input the input to be checked.
     */
    public boolean check(String input, boolean onlyAddWhenInFrontCharacterIsKnown) {
        int inputLength = input.length();
        int z = 0;
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
                        z++;
                    } else {
                        break;
                    }
                }
            }

            if (z == inputLength) {
                insertIndexes.addAll(tempInsertIndexes);
            }
        }

        if (onlyAddWhenInFrontCharacterIsKnown) {
            if (insertIndexes.get(0) == 0 || outputArray[insertIndexes.get(0) - 1] != '_') {
                for (int i : insertIndexes) {
                    outputArray[i] = solutionArray[i];
                }
                points++;
                return true;
            } else {
                setMistakeCircleNode();
            }
        } else {
            if (insertIndexes.size() == 0) {
                mistakes++;
                setMistakeCircleNode();
            } else {
                for (int i : insertIndexes) {
                    outputArray[i] = solutionArray[i];
                }

                points++;
                setMistakeCircleNode();
                return true;
            }
        }

        return false;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
