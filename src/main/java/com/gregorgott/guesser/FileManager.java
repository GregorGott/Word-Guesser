package com.gregorgott.guesser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Manages all file actions.
 *
 * @author GregorGott
 * @version 1.1.0
 * @since 2022-05-08
 */
public class FileManager {

    private static final File USER_PATH = new File(System.getProperty("user.home"));
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final File WG_FOLDER = new File(USER_PATH + FILE_SEPARATOR + ".Word-Guesser");

    private final Stage stage;
    private File file;

    public FileManager() {
        stage = new Stage();
    }

    /**
     * @return A selected text file.
     * @since 1.0.0
     */
    public File selectSingleplayerFile() {
        setScene();

        return file;
    }

    /**
     * Set the Scene with description label and two buttons for downloading and open Singleplayer files.
     *
     * @since 1.1.0
     */
    private void setScene() {
        Label label = new Label("Do you want to download or update all Singleplayer files? If you want to " +
                "select a file from your Computer click 'Open'.");
        label.setWrapText(true);

        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(x -> download());
        Button openButton = new Button("Open");
        openButton.setOnAction(x -> chooseFile());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(x -> closeStage());

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(downloadButton, openButton, cancelButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        borderPane.setTop(label);
        borderPane.setCenter(hBox);

        Scene scene = new Scene(borderPane);

        stage.setResizable(false);
        stage.setTitle("Singleplayer");
        stage.setWidth(300);
        stage.setHeight(120);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Clones the Word Guesser Singleplayer files from GitHub.
     *
     * @since 1.1.0
     */
    private void download() {
        if (WG_FOLDER.exists()) {
            delFolder(WG_FOLDER);
        }

        try {
            CloneCommand git = Git.cloneRepository()
                    .setURI("https://github.com/GregorGott/WG-Singleplayer-Files.git")
                    .setDirectory(WG_FOLDER);

            git.call();

            chooseFile();
        } catch (GitAPIException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An internet connection is required. Please try again later.");
            alert.setContentText(e.toString());

            alert.show();
        }
    }

    /**
     * Deletes the given file. If the file is a folder, it deletes all subfolders as well.
     *
     * @param file The file to delete.
     */
    private void delFolder(File file) {
        for (File subfile : Objects.requireNonNull(file.listFiles())) {
            if (subfile.isDirectory()) {
                delFolder(subfile);
            }

            subfile.delete();
        }

        file.delete();
    }

    /**
     * Choose a text file and close the Stage.
     *
     * @since 1.1.0
     */
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Singleplayer file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        file = fileChooser.showOpenDialog(null).getAbsoluteFile();

        closeStage();
    }

    /**
     * Closes the Stage.
     *
     * @since 1.1.0
     */
    private void closeStage() {
        stage.close();
    }

    /**
     * Get a file as input and scan through every line (ignore lines starting with ignoredLines) and return the number
     * of lines.
     *
     * @param file         File to scan.
     * @param ignoredLines Ignore lines starting with.
     * @return Number of lines.
     */
    public int countLines(File file, String ignoredLines) {
        int lines = 0;

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                if (!scanner.nextLine().startsWith(ignoredLines)) {
                    lines++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }
}