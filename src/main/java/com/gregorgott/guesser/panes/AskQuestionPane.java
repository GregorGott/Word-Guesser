package com.gregorgott.guesser.panes;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * The parent class for all game modes with mistakes and points counter.
 *
 * @author GregorGott
 * @version 1.0.0
 * @since 2022-06-04
 */
public class AskQuestionPane {
    private final ArrayList<Character> usedCharsList;
    private final int maxMistakes;
    private HBox circleHBox;
    private int points;
    private int mistakes;

    /**
     * The constructor initializes all needed variables and sets the points to zero.
     *
     * @param maxMistakes the max amount of allowed mistakes.
     * @since 1.0.0
     */
    public AskQuestionPane(int maxMistakes) {
        usedCharsList = new ArrayList<>();
        circleHBox = new HBox();
        points = 0;
        this.maxMistakes = maxMistakes;
    }

    /**
     * @return an array list with all used characters.
     * @since 1.0.0
     */
    public ArrayList<Character> getUsedCharsList() {
        return usedCharsList;
    }

    /**
     * This method is called to add a new character to the <code>usedCharsList</code>.
     *
     * @param character ist the character to be added.
     * @since 1.0.0
     */
    public void addUsedCharacter(char character) {
        usedCharsList.add(character);
    }

    /**
     * @return reached points as int.
     * @since 1.0.0
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param i is the new <code>points</code> value.
     * @since 1.0.0
     */
    public void setPoints(int i) {
        points = i;
    }

    /**
     * Add given points to <code>points</code>.
     *
     * @param i points to be added to <code>points</code>.
     * @since 1.0.0
     */
    public void addPoints(int i) {
        points = points + i;
    }

    /**
     * @return number of mistakes.
     * @since 1.0.0
     */
    public int getMistakes() {
        return mistakes;
    }

    /**
     * Adds one to <code>mistakes</code> and updates the <code>circleHBox</code>.
     *
     * @since 1.0.0
     */
    public void addMistake() {
        mistakes++;

        setCircleHBox();
    }

    /**
     * Sets an HBox with circle for each possible mistake (<code>maxMistakes</code>).
     *
     * @return the HBox with the mistake circles.
     * @since 1.0.0
     */
    public Node getCircleHBox() {
        Label mistakesLabel = new Label("Mistakes:");
        mistakesLabel.setId("white-label");

        circleHBox = new HBox();
        circleHBox.setAlignment(Pos.CENTER_LEFT);
        circleHBox.setSpacing(8);

        setCircleHBox();

        // HBox with enterACharLabel and mistake circles
        HBox mistakesHBox = new HBox();
        mistakesHBox.setSpacing(10);
        mistakesHBox.getChildren().addAll(mistakesLabel, circleHBox);

        return mistakesHBox;
    }

    /**
     * For each mistake a red and for each left mistake a green circle.
     *
     * @since 1.0.0
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
     * Get the circleHBox and deletes all children.
     *
     * @since 1.0.0
     */
    private void clearCircleHBox() {
        circleHBox.getChildren().clear();
    }

    /**
     * Add a circle with a given colour to the circleHBox.
     *
     * @param color The colour of the circle.
     * @since 1.0.0
     */
    private void addCircle(Color color) {
        Circle circle = new Circle(0, 0, 5, color);
        circleHBox.getChildren().add(circle);
    }
}