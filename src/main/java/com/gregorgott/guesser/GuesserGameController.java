package com.gregorgott.guesser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Game Controller controls the game (ask questions and set questions). Player 1 enters a word and Player 2
 * tries to guess it and so on. FXML Scene: guesser-game-scene.fxml
 *
 * @author GregorGott
 * @version 1.0.2
 * @since 2022-04-10
 */
public class GuesserGameController {
    // Declare FXML basic UI
    @FXML
    public BorderPane borderPane;

    @FXML
    public Label currentPlayerLabel;

    @FXML
    public Label questionsCounterLabel;

    @FXML
    public Button nextButton;

    private int currentPlayer = 1;
    private int currentRound = 1;
    private int pointsPlayer1, pointsPlayer2, tempPointsPlayer1, tempPointsPLayer2, numberOfQuestions,
            maxMistakes, mistakesCounter, wordLength;
    private boolean setQuestionTabActive = true;

    private char[] solutionArray, outputLabelArray;

    private List<Character> usedCharsList;

    private SetQuestionPane setQuestionPane;
    private AskQuestionPane askQuestionPane;

    /**
     * Start the game and set number of questions and max mistakes.
     * @param   numberOfQuestions   Number of questions/rounds in game.
     * @param   maxMistakes         Max amount of allowed mistakes per player.
     */
    public void startGame(int numberOfQuestions, int maxMistakes) {
        this.numberOfQuestions = numberOfQuestions;
        this.maxMistakes = maxMistakes;

        usedCharsList = new ArrayList<>();

        setQuestion();
    }

    /**
     * If the big 'next button' on the right side is pushed. Get current tab (setQuestion or askQuestion)
     * and switch tab. Example: If the current tab is the setQuestion tab set <code>setQuestionTabActive</code>
     * to false and switch to ask question.
     */
    public void buttonPushed() {
        if (setQuestionTabActive) {
            // Button is pushed in set question tab
            setQuestionTabActive = false;

            // Switch to next player
            if (currentPlayer == 1) {
                currentPlayer = 2;
            } else {
                currentPlayer = 1;
            }

            // Set center of border pane to askQuestion
            askQuestion();
        } else {
            currentRound++;

            // Show results if end is reached
            if (numberOfQuestions < currentRound) {
                openResultScene();
            } else {
                setQuestionTabActive = true;
                setQuestion();
            }
        }
    }

    /**
     * Get current round and player and show it on the top of the Scene.
     */
    private void setTopBarUI() {
        currentPlayerLabel.setText("Player " + currentPlayer + "'s turn");
        questionsCounterLabel.setText("Round " + currentRound + " of " + numberOfQuestions);
    }

    /**
     * Show the SetQuestionPane in the center of the border pane. If enter is pressed in text field call
     * <code>buttonPushed</code> method.
     */
    private void setQuestion() {
        setTopBarUI();

        setQuestionPane = new SetQuestionPane();
        setQuestionPane.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                buttonPushed();
            }
        });

        borderPane.setCenter(setQuestionPane.getPane());
    }

    /**
     * @return The text field text in uppercase.
     */
    private String convertTextFieldToUppercase() {
        return setQuestionPane.getTextFieldText().toUpperCase();
    }

    /**
     * Load word in <code>solutionArray</code> and loads an underscore for every character in <code>outputLabelArray</code>.
     * @param   word    Is the word to be guessed by the other player.
     */
    private void loadWordInArray(String word) {
        // Get length of wordToBeGuessed and set the array length
        wordLength = word.length();
        solutionArray = new char[wordLength];
        outputLabelArray = new char[wordLength];

        for (int i = 0; i < wordLength; i++) {
            // Load char at position i (counter var) into solution array
            solutionArray[i] = word.charAt(i);
            // Load underlines/underscores into output label array
            outputLabelArray[i] = '_';
        }
    }

    /**
     * Show a text field, a button and a scroll pane with a label on it. The player enters his guess (one character)
     * in the text field and clicks on the <code>checkGuessButton</code> button. The <code>checkGuess</code> method
     * checks the guess.
     * @see <a href="https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength">
     *     JavaFX 2.2 TextField maxlength</a>
     */
    private void askQuestion() {
        setTopBarUI();

        loadWordInArray(convertTextFieldToUppercase());

        // Disable nextButton on right side
        nextButton.setDisable(true);

        askQuestionPane = new AskQuestionPane(outputLabelArray, maxMistakes);
        // Press enter instead of pushing the 'checkGuessButton' button
        askQuestionPane.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                checkGuess();
            }
        });
        askQuestionPane.checkGuessButton.setOnAction(event -> checkGuess());

        borderPane.setCenter(askQuestionPane.getPane());
    }

    /**
     * Get character from askQuestionPane textField and check if the solution array contains the character. If not, add
     * the character to usedCharsList and add one mistake circle. If the max amount of mistakes is reached, show the
     * solution and disable the askQuestionPane buttons.
     * Is the character in the solution array show where the character is used in the word.
     */
    private void checkGuess() {
        // Get character and clear the text field
        String singleChar = askQuestionPane.getTextField();
        askQuestionPane.textField.setText("");

        // If text field is not empty
        if (!singleChar.isEmpty()) {
            // Convert the character to uppercase
            char charFromInput = singleChar.toUpperCase().charAt(0);

            // If character is not already in the usedCharsList
            if (!usedCharsList.contains(charFromInput)) {
                // If character is in the array
                if (isCharInArray(charFromInput, solutionArray)) {
                    for (int i = 0; i < wordLength; i++) {
                        if (charFromInput == solutionArray[i]) {
                            // Set outputLabelArray at index i to solutionArray at index i
                            outputLabelArray[i] = solutionArray[i];
                            askQuestionPane.outputLabel.setText("");

                            // Show new outputLabelArray
                            for (int i2 = 0; i2 < wordLength; i2++) {
                                askQuestionPane.outputLabel.setText(
                                        askQuestionPane.outputLabel.getText() + " " + outputLabelArray[i2] + " ");
                            }

                            // Add one point to player
                            if (currentPlayer == 1) {
                                tempPointsPlayer1++;
                            } else {
                                tempPointsPLayer2++;
                            }

                            // If the solution is found, add temporary points to permanent points and end the round
                            if (Arrays.equals(solutionArray, outputLabelArray)) {
                                pointsPlayer1 += tempPointsPlayer1;
                                pointsPlayer2 += tempPointsPLayer2;

                                endRound();
                            }
                        }
                    }
                } else {
                    // Character is not in array
                    // Add 1 to mistake counter and add character to usedCharsList
                    mistakesCounter++;
                    usedCharsList.add(charFromInput);
                    askQuestionPane.addUsedCharacter(charFromInput);

                    // If max amount of mistakes is reached
                    if (mistakesCounter == maxMistakes) {
                        // Switch all circles to red and end round
                        askQuestionPane.clearCircleHBox();
                        for (int i = 0; i < maxMistakes; i++) {
                            askQuestionPane.addCircle(Color.rgb(152,0,0));
                        }

                        endRound();
                    } else {
                        // Clear mistake circles and add one red circle
                        askQuestionPane.clearCircleHBox();

                        for (int i = 0; i < mistakesCounter; i++) {
                            askQuestionPane.addCircle(Color.rgb(176,44,44));
                        }
                        for (int i = 0; i < maxMistakes - mistakesCounter; i++) {
                            askQuestionPane.addCircle( Color.rgb(27,94,23));
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if value is in chars array.
     * @param   value   This char is checked.
     * @param   chars   The value is searched in this array.
     * @return          A boolean if the value is in the array.
     */
    private boolean isCharInArray(char value, char[] chars) {
        for (char c : chars) {
            if (c == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Disable 'Check Guess' button and text field in askQuestion, show the solution and restore all variables.
     */
    private void endRound() {
            // Disable 'Check Guess' button and 'textField', and enable 'nextButton'
            nextButton.setDisable(false);
            askQuestionPane.textField.setDisable(true);
            askQuestionPane.checkGuessButton.setDisable(true);

            // Show solution
            askQuestionPane.outputLabel.setText("");
            for (char c : solutionArray) {
                askQuestionPane.outputLabel.setText(askQuestionPane.outputLabel.getText() + " " + c + " ");
            }

            // Restore variables to default
            mistakesCounter = 0;
            tempPointsPlayer1 = 0;
            tempPointsPLayer2 = 0;
            usedCharsList.clear();
    }

    /**
     * Show an alert and end round when user clicks on 'OK'.
     */
    public void cancelRoundButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("End round");
        alert.setContentText("The round will end immediately, the result may not be correct.");

        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            openResultScene();
        }
    }

    /**
     * Get winner and set a winner text. Show the winner text in ResultSceneController.
     */
    public void openResultScene() {
        // Winner text
        String winner;

        if (pointsPlayer2 > pointsPlayer1) {
            winner = "Player 2 won";
        } else if (pointsPlayer2 < pointsPlayer1) {
            winner = "Player 1 won";
        } else {
            winner = "Draw!";
        }

        // Load result screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("result-scene.fxml"));
            Parent root = loader.load();

            ResultSceneController resultSceneController = loader.getController();
            resultSceneController.setWinnerLabel(winner);

            Stage stage = (Stage) borderPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}