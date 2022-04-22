package com.gregorgott.guesser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * The Game Controller controls the game (ask questions and set questions). Player 1 enters a word and Player 2
 * tries to guess it and so on. FXML Scene: guesser-game-scene.fxml
 *
 * @author GregorGott
 * @version 1.1.0
 * @since 2022-04-21
 */
public class GuesserGameController {
    private final ArrayList<Character> usedCharsList;
    private final ArrayList<String> usedWordsList;
    private final FileManager fileManager;

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
    private boolean isSetQuestionTabActive = true;
    private char[] solutionArray, outputLabelArray;
    private File pathToGuessingFile;
    private GameType gameType;
    private SetQuestionPane setQuestionPane;
    private AskQuestionPane askQuestionPane;

    /**
     * Initialize basic array lists and FileManager.
     */
    public GuesserGameController() {
        fileManager = new FileManager();

        usedCharsList = new ArrayList<>();
        usedWordsList = new ArrayList<>();
    }

    /**
     * Start the game and set a number of questions and max mistakes. When the multiplayer mode is selected,
     * set a question, but when the Singleplayer mode is selected, set the source file with random words.
     *
     * @param numberOfQuestions Number of questions/rounds in the game.
     * @param maxMistakes       Max amount of allowed mistakes per player.
     * @param gameType          Single or Multiplayer mode.
     */
    public void startGame(int numberOfQuestions, int maxMistakes, GameType gameType) {
        this.numberOfQuestions = numberOfQuestions;
        this.maxMistakes = maxMistakes;
        this.gameType = gameType;

        if (gameType == GameType.MULTIPLAYER) {
            setQuestion();
        } else if (gameType == GameType.SINGLEPLAYER) {
            setSingleplayerWordList();
            loadRandomWordInArray();
        }
    }

    /**
     * If the big 'next button' on the right side is pushed. Get current tab (setQuestion or askQuestion)
     * and switch tab. Example: If the current tab is the setQuestion tab set setQuestionTabActive
     * to false and switch to ask question.
     */
    public void buttonPushed() {
        if (gameType == GameType.MULTIPLAYER) {
            if (isSetQuestionTabActive) {
                // Button is pushed in set question tab
                isSetQuestionTabActive = false;

                // Switch to next player
                if (currentPlayer == 1) {
                    currentPlayer = 2;
                } else {
                    currentPlayer = 1;
                }

                // Get word from setQuestionPane and load it in an Array and show the question
                loadWordInArray(convertStringToUppercase(setQuestionPane.getTextFieldText()));
                askQuestion();
            } else {
                currentRound++;

                // Show results if end is reached
                if (numberOfQuestions < currentRound) {
                    showResultScene();
                } else {
                    isSetQuestionTabActive = true;
                    setQuestion();
                }
            }
        } else if (gameType == GameType.SINGLEPLAYER) {
            currentRound++;

            if (numberOfQuestions < currentRound) {
                showResultScene();
            } else {
                setTopBarUI();
                loadRandomWordInArray();
            }
        }
    }

    /**
     * Get the current round and player and show it on the top of the Scene.
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
     * Set pathToGuessing file and check the number of lines in the file. If there are fewer lines than selected amount
     * of questions, set questions to number of lines.
     *
     * @param pathToGuessingFile Path to the text file with words.
     */
    public void setPathToGuessingFile(File pathToGuessingFile) {
        this.pathToGuessingFile = pathToGuessingFile;

        // If there are fewer lines than questions set number of questions to number of lines in file.
        int linesInFile = fileManager.countLines(pathToGuessingFile, "##");
        if (numberOfQuestions > linesInFile) {
            numberOfQuestions = linesInFile;
        }
    }

    /**
     * Get all lines from pathToGuessingFile and load every line wich not start with a "##"
     * in singleplayerWordList ArrayList.
     */
    private void setSingleplayerWordList() {
        try {
            Scanner scanner = new Scanner(pathToGuessingFile);
            while (scanner.hasNextLine()) {
                // Load line in String
                String scannerNextLine = scanner.nextLine();
                // Detect file comments
                if (!scannerNextLine.startsWith("##")) {
                    usedWordsList.add(scannerNextLine);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a random word from the selected file, load it in the Array and ask the question.
     */
    private void loadRandomWordInArray() {
        // Get random number between 0 and the size of the wordsInArray Array.
        Random randomWord = new Random();
        int line = randomWord.nextInt(usedWordsList.size());

        // Load it in the Array and ask question
        loadWordInArray(convertStringToUppercase(usedWordsList.get(line)));

        // Remove line to avoid a second ask of the questions
        usedWordsList.remove(line);

        askQuestion();
    }

    /**
     * @param string The String to be converted to uppercase.
     * @return The text field text in uppercase.
     */
    private String convertStringToUppercase(String string) {
        return string.toUpperCase();
    }

    /**
     * Load word in <code>solutionArray</code> and loads an underscore for every character in <code>outputLabelArray</code>.
     *
     * @param word Is the word to be guessed by the other player.
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
     * Show the askQuestion Pane and check the users input when clicking on the checkGuessButton or pressing Enter.
     */
    private void askQuestion() {
        setTopBarUI();

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
                            askQuestionPane.addCircle(Color.rgb(152, 0, 0));
                        }

                        endRound();
                    } else {
                        // Clear mistake circles and add one red circle
                        askQuestionPane.clearCircleHBox();

                        for (int i = 0; i < mistakesCounter; i++) {
                            askQuestionPane.addCircle(Color.rgb(176, 44, 44));
                        }
                        for (int i = 0; i < maxMistakes - mistakesCounter; i++) {
                            askQuestionPane.addCircle(Color.rgb(27, 94, 23));
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if the value is in the chars array.
     *
     * @param value This char is checked.
     * @param chars The value is searched in this array.
     * @return A boolean if the value is in the array.
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
     * Disable the 'Check Guess' button and the text field in askQuestion, show the solution and restore all variables.
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
     * Show an alert and end round when the user clicks on 'OK'.
     */
    public void cancelRoundButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("End round now.");
        alert.setContentText("The round will end immediately, the result may not be correct.");

        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            showResultScene();
        }
    }

    /**
     * Get the winner and set a winner text. Show the winner text in ResultSceneController.
     */
    private void showResultScene() {
        // Winner text
        String winner = null;

        if (gameType == GameType.MULTIPLAYER) {
            if (pointsPlayer2 > pointsPlayer1) {
                winner = "Player 2 won.";
            } else if (pointsPlayer2 < pointsPlayer1) {
                winner = "Player 1 won.";
            } else {
                winner = "Draw!";
            }
        } else if (gameType == GameType.SINGLEPLAYER) {
            winner = pointsPlayer1 + "Punkte erreicht";
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

    /**
     * Multiplayer is the normal game mode with two players. One is guessing the word, the other one gives.
     * In Singleplayer, the computer searches for a random word and the player tries to guess it.
     */
    public enum GameType {
        SINGLEPLAYER,
        MULTIPLAYER
    }
}
