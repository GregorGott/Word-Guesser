package com.gregorgott.guesser;

import com.gregorgott.guesser.panes.AskQuestionPane;
import com.gregorgott.guesser.panes.SetQuestionPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
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
 * @version 1.1.5
 * @since 2022-04-30
 */
public class GuesserGameController {
    private final ArrayList<String> singleplayerWordsList;
    private final FileManager fileManager;

    // Declare FXML basic UI
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private Label questionsCounterLabel;
    @FXML
    private ProgressBar roundProgressBar;

    private int currentPlayer = 1;
    private int currentRound = 1;
    private int pointsPlayer1, pointsPlayer2, numberOfQuestions, maxMistakes, pointsToBeReached;
    private boolean isSetQuestionTabActive;
    private char[] solutionArray;
    private File pathToGuessingFile;
    private GameType gameType;
    private SetQuestionPane setQuestionPane;
    private AskQuestionPane askQuestionPane;

    /**
     * Initialize basic array lists and FileManager.
     */
    public GuesserGameController() {
        fileManager = new FileManager();
        isSetQuestionTabActive = true;
        singleplayerWordsList = new ArrayList<>();
    }

    /**
     * Start the game and set the number of questions and max mistakes. When the multiplayer mode is selected,
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
            setNumberOfQuestions();
            setSingleplayerWordList();
            loadRandomWordInArray();
        }
    }

    /**
     * If the big 'next button' on the right side is pushed. Get the current tab (setQuestion or askQuestion)
     * and switch tab. Example: If the current tab is the setQuestion tab, set setQuestionTabActive
     * to false and switch to askQuestionPane.
     */
    public void buttonPushed() {
        if (gameType == GameType.MULTIPLAYER) {
            if (isSetQuestionTabActive) {
                isSetQuestionTabActive = false;

                // Get word from setQuestionPane and load it in an Array and show the question
                loadWordInArray(convertStringToUppercase(setQuestionPane.getWordToBeGuessed()));
                askQuestion();
            } else {
                if (askQuestionPane.isFinished()) {
                    isSetQuestionTabActive = true;

                    currentRound++;

                    if (currentPlayer == 1) {
                        pointsPlayer1 = askQuestionPane.getPoints();
                        currentPlayer = 2;
                    } else {
                        pointsPlayer2 = askQuestionPane.getPoints();
                        currentPlayer = 1;
                    }

                    if (numberOfQuestions < currentRound) {
                        // Show results if end is reached
                        showResultScene();
                    } else {
                        setQuestion();
                    }
                }
            }
        } else {
            currentRound++;

            if (numberOfQuestions < currentRound) {
                // Show results if end is reached
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

        setRoundProgressBar();
    }

    /**
     * Set the progress bar at the top to show the round progress.
     */
    private void setRoundProgressBar() {
        roundProgressBar.setProgress((double) currentRound / numberOfQuestions);
    }

    /**
     * Show the SetQuestionPane in the center of the border pane. If enter is pressed in text field call
     * <code>buttonPushed</code> method.
     */
    private void setQuestion() {
        setTopBarUI();

        setQuestionPane = new SetQuestionPane();
        setQuestionPane.getWordToGuessTextField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                buttonPushed();
            }
        });

        borderPane.setCenter(setQuestionPane.getPane());
    }

    /**
     * Set the pathToGuessing file and check the number of lines in the file.
     *
     * @param pathToGuessingFile Path to the text file with words.
     */
    public void setPathToGuessingFile(File pathToGuessingFile) {
        this.pathToGuessingFile = pathToGuessingFile;
    }

    /**
     * Check how many lines the pathToGuessingFile file has. If there are fewer lines than the selected amount
     * of questions, set questions to the number of lines.
     */
    private void setNumberOfQuestions() {
        // If there are fewer lines than questions set number of questions to number of lines in file.
        int linesInFile = fileManager.countLines(pathToGuessingFile, "##");
        if (linesInFile < numberOfQuestions) {
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
                    singleplayerWordsList.add(scannerNextLine);
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
        int line = randomWord.nextInt(singleplayerWordsList.size());

        // Load it in the Array and ask question
        loadWordInArray(convertStringToUppercase(singleplayerWordsList.get(line)));

        // Remove line to avoid a second ask of the questions
        singleplayerWordsList.remove(line);

        pointsToBeReached += calculatePointsToBeReached();

        askQuestion();
    }

    /**
     * Calculate how many points the player can reach.
     *
     * @return The number of points the player can reach.
     */
    private int calculatePointsToBeReached() {
        // Sort solution array to get duplicates in array
        char[] sortedSolutionArray = solutionArray.clone();
        Arrays.sort(sortedSolutionArray);

        // A String must contain at least one character
        // E.g.: "ooooooooo" contains one unique character "o"
        int counter = 1;

        // Check if element in array is the same one as one index after
        for (int i = 0; i < sortedSolutionArray.length - 1; i++) {
            if (sortedSolutionArray[i] != sortedSolutionArray[i + 1]) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * @param string The string to be converted to uppercase.
     * @return The text field text in uppercase.
     */
    private String convertStringToUppercase(String string) {
        return string.toUpperCase();
    }

    /**
     * Load word in solutionArray and loads an underscore for every character in outputLabelArray.
     *
     * @param word Is the word to be guessed by the other player.
     */
    private void loadWordInArray(String word) {
        // Get length of wordToBeGuessed and set the array length
        int wordLength = word.length();
        solutionArray = new char[wordLength];

        for (int i = 0; i < wordLength; i++) {
            // Load char at position i (counter var) into solution array
            solutionArray[i] = word.charAt(i);
        }
    }

    /**
     * Show the askQuestion Pane and check the user input when clicking on the checkGuessButton or pressing Enter.
     */
    private void askQuestion() {
        setTopBarUI();

        askQuestionPane = new AskQuestionPane(solutionArray, maxMistakes);
        borderPane.setCenter(askQuestionPane.getRoot());
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
        // Set winner text
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
            winner = "Score " + pointsPlayer1 + " out of " + pointsToBeReached + ".";
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

