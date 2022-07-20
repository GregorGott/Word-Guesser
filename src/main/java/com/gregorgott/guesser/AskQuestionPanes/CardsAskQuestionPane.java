package com.gregorgott.guesser.AskQuestionPanes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Random;

/**
 * The cards question mode is simpler than the classic mode, because each character is shown in a button.
 *
 * @author GregorGott
 * @version 1.1.2
 * @since 2022-06-14
 */
public class CardsAskQuestionPane extends AskQuestionManager {
    private final Label outputLabel;
    private final ArrayList<Character> sortedArray;
    private final ArrayList<Button> buttons;
    private Node root;
    private boolean finished;

    /**
     * The constructor initializes needed variables and fills the solution array with underscores for each character.
     *
     * @since 1.0.0
     */
    public CardsAskQuestionPane() {
        super();
        outputLabel = new Label();

        sortedArray = new ArrayList<>();
        buttons = new ArrayList<>();

        finished = false;
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

        setSortedArray();

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

        VBox vBox = new VBox(scrollPane, characterButtonsFlowPane);//, getCirclesPane());
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
            button.setOnAction(x -> {
                if (check(s ,true)) {
                    setOutputLabel();
                    button.setDisable(true);
                }
            });
            sortedArray.remove(Character.valueOf(s.charAt(0)));
            buttons.add(button);
        }
    }

    /**
     * Creates an array list, which only contains each character in the solution array once.
     *
     * @since 2022-06-03
     */
    private void setSortedArray() {
        for (char c : getSolutionArray()) {
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
        outputLabel.setText(getOutputArrayAsString());
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

    /**
     * @return The root Pane with all elements.
     */
    @Override
    public Node getNode() {
        setRoot();
        return root;
    }
}
