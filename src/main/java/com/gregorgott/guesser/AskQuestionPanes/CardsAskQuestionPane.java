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
 * This class can be implemented over the <code>AskQuestionManager</code>:
 * <pre>
 *     private void myClass() {
 *          AskQuestionManager askQuestionManager = new CardsAskQuestionPane();
 *     }
 * </pre>
 *
 * @author GregorGott
 * @version 1.1.3
 * @since 2022-07-22
 */
public class CardsAskQuestionPane extends AskQuestionManager {
    private final Label outputLabel;
    private final ArrayList<Character> sortedArray;
    private final ArrayList<Button> buttons;
    private Node root;

    /**
     * Initializes the <code>outputLabel</code> label, <code>sortedArray</code> ArrayList and the <code>buttons</code>
     * ArrayList.
     *
     * @since 1.0.0
     */
    public CardsAskQuestionPane() {
        super();
        outputLabel = new Label();

        sortedArray = new ArrayList<>();
        buttons = new ArrayList<>();
    }

    /**
     * Sets the root pane with a scroll pane which includes the output array and a flow pane with the character
     * buttons. All these elements are children of a <code>VBox</code> which defines the <code>root</code> Node.
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

        VBox vBox = new VBox(scrollPane, characterButtonsFlowPane, getMistakeCirclesNode());
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
            // Loads a random string from the sortedArray
            String s = String.valueOf(getRandomFromArray(sortedArray));

            // Creates a new button with the string and removes the random string from the sortedArray
            Button button = new Button(s);
            button.setOnAction(x -> checkAndUpdate(button));
            sortedArray.remove(Character.valueOf(s.charAt(0)));
            buttons.add(button);
        }
    }

    /**
     * Checks if the selected button is valid, means if the character before is already known it counts as a point.
     * Afterwards, check if the game is finished and disable all buttons.
     *
     * @param b the pressed button.
     * @since 1.1.3
     */
    private void checkAndUpdate(Button b) {
        String s = b.getText();
        if (check(s ,true, true)) {
            setOutputLabel();
            b.setDisable(true);
        }

        if (isFinished()) {
            for (Button button : buttons) {
                button.setDisable(true);
            }
            buttons.clear();
        }
    }

    /**
     * Creates an <code>ArrayList</code>, which only contains each character in the <code>solutionArray</code> once.
     *
     * @since 1.1.0
     */
    private void setSortedArray() {
        for (char c : getSolutionArray()) {
            if (!sortedArray.contains(c)) {
                sortedArray.add(c);
            }
        }
    }

    /**
     * Sets the <code>outputLabel</code> to the <code>outputArray</code> as string.
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

    /**
     * @return The root Pane with all elements.
     */
    @Override
    public Node getNode() {
        setRoot();
        return root;
    }
}
