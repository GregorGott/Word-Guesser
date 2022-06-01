package com.gregorgott.guesser.panes;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Random;

public class CardsAskQuestionPane {
    private final char[] solutionArray;
    private final char[] outputArray;
    private final char[] sortedArray;
    private final char[] lettersArray;
    private int points;
    private int mistakes;
    private boolean finished;
    private final Pane root;
    private Label outputLabel;
    private Button[] buttons;

    public CardsAskQuestionPane(char[] solutionArray) {
        this.solutionArray = solutionArray;
        outputArray = new char[solutionArray.length];
        sortedArray = createSortedArray();
        buttons = new Button[10];

        for (int i = 0; i < solutionArray.length; i++) {
            if (solutionArray[i] == ' ') {
                outputArray[i] = ' ';
            } else {
                outputArray[i] = '_';
            }
        }

        lettersArray = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        points = 0;
        mistakes = 0;
        finished = false;

        root = new Pane();
        outputLabel = new Label();

        setRoot();
    }

    public Pane getRoot() {
        return root;
    }

    private void setRoot() {
        setOutputLabel();

        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(50);
        scrollPane.setPadding(new Insets(8));

        FlowPane characterButtonsFlowPane = new FlowPane();
        characterButtonsFlowPane.getChildren().addAll(createCharacterButtons());

        VBox vBox = new VBox(scrollPane, characterButtonsFlowPane);
        root.getChildren().add(vBox);
    }

    private Button[] createCharacterButtons() {
        buttons = new Button[10];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button();
            buttons[i].setPrefWidth(50);
            buttons[i].setPrefHeight(50);
            int finalI = i;
            buttons[i].setOnAction(x -> checkButton(buttons[finalI].getText().charAt(0)));
        }

        int j = 0;

        for (int i = 0; i < 5 && i < sortedArray.length; i++) {
            buttons[i].setText(String.valueOf(getRandomFromArray(sortedArray)));
            j++;
        }

        for (int i = j; i < 10; i++) {
            buttons[i].setText(String.valueOf(getRandomFromArray(lettersArray)));
        }

        return buttons;
    }

    private void checkButton(ActionEvent e, char c) {
        if (checkIfArrayContains(solutionArray, c)) {
            for (int i = 0; i < solutionArray.length; i++) {
                if (solutionArray[i] == c) {
                    outputArray[i] = solutionArray[i];
                    setOutputLabel();
                    replaceButton((Button) e.getSource());
                }
            }
        }
    }

    private boolean checkIfArrayContains(char[] array, char key) {
        for (char a : array) {
            if (a == key) {
                return true;
            }
        }
        return false;
    }

    private void replaceButton(Button button) {
        button.setText(getRandomFromArray(let));
    }

    private char[] createSortedArray() {
        ArrayList<Character> tempList = new ArrayList<>();

        for (char c : solutionArray) {
            if (!tempList.contains(c)) {
                tempList.add(c);
            }
        }

        int newArrayLength = tempList.size();

        char[] sortedArray = new char[newArrayLength];

        for (int i = 0; i < newArrayLength; i++) {
            sortedArray[i] = tempList.get(i);
        }

        return sortedArray;
    }

    private void setOutputLabel() {
        outputLabel.setText("");
        for (char c : outputArray) {
            outputLabel.setText(outputLabel.getText() + c + " ");
        }
    }

    private char getRandomFromArray(char[] array) {
        int random = new Random().nextInt(array.length);

        return array[random];
    }
}
