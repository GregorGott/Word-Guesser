package com.gregorgott.guesser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuesserGameController {

    @FXML
    public BorderPane borderPane;

    @FXML
    public Label currentPlayerLabel;

    @FXML
    public Label questionsCounterLabel;

    @FXML
    public Button nextButton;

    private HBox circleHBox;

    private final TextField setWordTextField = new TextField();
    private final TextField singleCharTextField = new TextField();

    private Button checkButton;

    private final Label outputLabel = new Label();
    private final Label usedCharsLabel = new Label();

    int currentPlayer = 1;
    int currentRound = 1;
    int pointsPlayer1, pointsPlayer2, tempPointsPlayer1, tempPointsPLayer2;
    int numberOfQuestions;
    int maxMistakes;
    int mistakesCounter;
    boolean createQuestion = true;

    char[] wordToBeGuessedSolution;
    char[] outputLabelArray;

    List<Character> usedCharsList = new ArrayList<>();

    public void setBasics(int i1, int i2) {
        numberOfQuestions = i1;
        maxMistakes = i2;

        setQuestion();
    }

    public void buttonPushed() throws IOException {
        // Check current Tab
        //      if createQuestion == true
        //          nextButton was pressed in "setQuestion" mode
        //      if createQuestion == false
        //          nextButton was pressed in "askQuestion" mode

        if (createQuestion) {
            createQuestion = false;

            // Switch to next player
            if (currentPlayer == 1) {
                currentPlayer = 2;
            } else {
                currentPlayer = 1;
            }

            int wordToBeGuessedLength = setWordTextField.getLength();

            // Get length of wordToBeGuessed and set the array length
            wordToBeGuessedSolution = new char[wordToBeGuessedLength];
            outputLabelArray = new char[wordToBeGuessedLength];

            // Convert the word into uppercase
            String wordInUpperCase = setWordTextField.getText().toUpperCase();

            for (int i = 0; i < wordToBeGuessedLength; i++) {
                // Load char at position i (counter var) into solution array
                wordToBeGuessedSolution[i] = wordInUpperCase.charAt(i);

                // Load underlines/underscores into output label array
                outputLabelArray[i] = '_';
            }

            askQuestion();
        } else {
            currentRound++;

            // Show results if end is reached
            if (numberOfQuestions < currentRound) {
                openResultScene();
            } else {
                createQuestion = true;
                setQuestion();
            }
        }

    }

    private void setQuestion() {
        // Basic UI
        currentPlayerLabel.setText("Player " + currentPlayer + "'s turn");
        questionsCounterLabel.setText("Round " + currentRound + " of " + numberOfQuestions);

        Label label = new Label("Enter a word:");

        setWordTextField.setText("Caretaker");
        setWordTextField.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            // Word needs to be longer than two words
            nextButton.setDisable(setWordTextField.getText().length() < 2);
        });
        setWordTextField.setOnKeyPressed(event -> {
            try {
                buttonPushed();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        VBox.setMargin(setWordTextField, new Insets(10, 0, 0, 0));

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.getChildren().addAll(label, setWordTextField);

        BorderPane.setMargin(vBox, new Insets(5, 5, 5, 5));
        borderPane.setCenter(vBox);
    }

    private void askQuestion() {
        // Basic UI
        currentPlayerLabel.setText("PLayer " + currentPlayer + "'s turn");

        // Restore variables to default
        nextButton.setDisable(true);

        Label label = new Label("Enter a character:");

        // The user may enter only one letter in the field
        // The listener avoids entering more than one character
        // Code help from: https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength (ceklock)
        singleCharTextField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (singleCharTextField.getText().length() > 1) {
                String s = singleCharTextField.getText().substring(0, 1);
                singleCharTextField.setText(s);
            }
        });
        // Press enter instead of pushing the 'checkButton'
        singleCharTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                checkGuess(null);
            }
        });
        singleCharTextField.setDisable(false);

        checkButton = new Button("Check Guess");
        checkButton.setFocusTraversable(false);
        checkButton.setDisable(false);
        checkButton.setOnAction(this::checkGuess);

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        VBox.setMargin(hBox, new Insets(10, 0, 10, 0));
        hBox.getChildren().addAll(singleCharTextField, checkButton);

        // Underline/Underscore for each word in "word to be guessed"
        outputLabel.setText("");
        for (int i = 0; i < wordToBeGuessedSolution.length; i++) {
            outputLabel.setText(outputLabel.getText() + " " + outputLabelArray[i] + " ");
        }

        // ScrollPane to scroll if the word is too long
        ScrollPane scrollPane = new ScrollPane(outputLabel);
        scrollPane.setPrefHeight(50);
        scrollPane.setFocusTraversable(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPadding(new Insets(10,10,10,10));

        // Info box under scroll pane - pointsInfoVBox
        //      @circleHBox             - Contains the mistake circles
        //      @mistakeHeadLabel       - "Mistakes"
        //      @mistakeCirclesHBox     - Contains circleHBox and mistakeHeadLabel
        //      @pointsInfoVBox         - Contains all these elements (Mistake circles and already used chars)

        circleHBox = new HBox();
        circleHBox.setAlignment(Pos.CENTER_LEFT);
        circleHBox.setSpacing(8);

        // Create circle for each mistake
        for (int i = 0; i < maxMistakes; i++) {
            Circle circle = new Circle(0, 0, 5, Color.rgb(27,94,23));
            circleHBox.getChildren().add(circle);
        }

        Label mistakeHeadLabel = new Label("Mistakes:");

        HBox mistakeCirclesHBox = new HBox();
        mistakeCirclesHBox.setSpacing(25);
        mistakeCirclesHBox.getChildren().addAll(mistakeHeadLabel, circleHBox);

        VBox pointsInfoVBox = new VBox();
        pointsInfoVBox.setPadding(new Insets(8, 8, 8, 8));
        pointsInfoVBox.setSpacing(10);
        pointsInfoVBox.getChildren().addAll(mistakeCirclesHBox, usedCharsLabel);

        VBox.setMargin(pointsInfoVBox, new Insets(10, 0, 0, 0));

        // Add pointsInfoVBox to view
        VBox mainVBox = new VBox();
        mainVBox.setPadding(new Insets(10, 10, 10, 10));
        mainVBox.getChildren().addAll(label, hBox, scrollPane, pointsInfoVBox);

        BorderPane.setMargin(mainVBox, new Insets(5, 5, 5, 5));
        borderPane.setCenter(mainVBox);
    }

    private void checkGuess(ActionEvent event) {
        // Check Guess if text is not empty
        if (!singleCharTextField.getText().isEmpty()) {
            // Converts the input text to uppercase letters
            char singleCharFromInput = (singleCharTextField.getText().toUpperCase()).charAt(0);

            // If the char is not already entered
            if (!usedCharsList.contains(singleCharFromInput)) {
                // If wordToBeGuessedSolution contains charFromInput else add to mistakes
                if (checkArrayValue(singleCharFromInput, wordToBeGuessedSolution)) {
                    // Repeat with each letter of the word to be guessed
                    for (int i = 0; i < wordToBeGuessedSolution.length; i++) {
                        // Get character at postion i (counter variable)
                        char character = wordToBeGuessedSolution[i];

                        // If input is character add char to output
                        if (singleCharFromInput == character) {
                            // Add the character at position i to output
                            outputLabelArray[i] = wordToBeGuessedSolution[i];
                            outputLabel.setText("");

                            for (int i2 = 0; i2 < wordToBeGuessedSolution.length; i2++) {
                                outputLabel.setText(outputLabel.getText() + " " + outputLabelArray[i2] + " ");
                            }

                            // Add point to current player
                            // Temporary points are added to the points only when the question is completed
                            if (currentPlayer == 1) {
                                tempPointsPlayer1++;
                            } else {
                                tempPointsPLayer2++;
                            }

                            // When the complete solution is found disable input options
                            if (Arrays.equals(wordToBeGuessedSolution, outputLabelArray)) {
                                // Add temp points to permanent points
                                pointsPlayer1 += tempPointsPlayer1;
                                pointsPlayer2 += tempPointsPLayer2;

                                endRound();
                            }
                        }
                    }
                } else {
                    // Add mistake
                    mistakesCounter++;

                    if (maxMistakes == mistakesCounter) {
                        // Game Over -> Switch all circles
                        circleHBox.getChildren().clear();
                        for (int i = 0; i < maxMistakes; i++) {
                            Circle circle = new Circle(0, 0, 5, Color.rgb(152,0,0));
                            circleHBox.getChildren().add(circle);
                        }

                        endRound();
                    } else {
                        // Plus one mistake
                        circleHBox.getChildren().clear();

                        // Add used char to mistake list
                        usedCharsList.add(singleCharFromInput);
                        updateUsedCharsLabel();

                        // Add one red circle
                        for (int i = 0; i < mistakesCounter; i++) {
                            Circle circle = new Circle(0, 0, 5, Color.rgb(176,44,44));
                            circleHBox.getChildren().add(circle);
                        }
                        for (int i = 0; i < maxMistakes - mistakesCounter; i++) {
                            Circle circle = new Circle(0, 0, 5, Color.rgb(27,94,23));
                            circleHBox.getChildren().add(circle);
                        }
                    }
                }
            }
        }

        singleCharTextField.setText("");
    }

    private boolean checkArrayValue(char value, char[] chars) {
        for (char aChar : chars) {
            if (aChar == value) {
                return true;
            }
        }
        return false;
    }

    private void updateUsedCharsLabel() {
        usedCharsLabel.setText("Already used characters: " + usedCharsList.get(0));

        for (int i = 1; i < usedCharsList.size(); i++) {
            usedCharsLabel.setText(usedCharsLabel.getText() + ", " + usedCharsList.get(i));
        }
    }

    private void endRound() {
            // Disable all buttons
            singleCharTextField.setDisable(true);
            nextButton.setDisable(false);
            checkButton.setDisable(true);

            // Show solution if game over
            outputLabel.setText("");
            for (char c : wordToBeGuessedSolution) {
                outputLabel.setText(outputLabel.getText() + " " + c + " ");
            }

            // Restore variables to default
            mistakesCounter = 0;
            tempPointsPlayer1 = 0;
            tempPointsPLayer2 = 0;
            usedCharsList.clear();
            usedCharsLabel.setText("");
    }

    public void cancelRoundButtonPushed() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("End round");
        alert.setContentText("The round will end immediately, the result may not be correct.");

        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            openResultScene();
        }
    }

    public void openResultScene() throws IOException {
        // Winner text
        String winner;

        if (pointsPlayer1 < pointsPlayer2) {
            winner = "Player 2 won";
        } else if (pointsPlayer2 < pointsPlayer1) {
            winner = "Player 1 won";
        } else {
            winner = "Draw!";
        }

        // Load result screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("result-scene.fxml"));
        Parent root = loader.load();

        ResultSceneController resultSceneController = loader.getController();
        resultSceneController.setWinnerLabel(winner);

        Stage stage = (Stage) borderPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}