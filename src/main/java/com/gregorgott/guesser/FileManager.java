package com.gregorgott.guesser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.Desktop;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

/**
 * Manages all file actions.
 *
 * @author GregorGott
 * @version 1.1.2
 * @since 2022-05-10
 */
public class FileManager {
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
        Label label = new Label("Do you want to download all Singleplayer files? If you want to " +
                "select a file from your Computer, click 'Open'.");
        label.setWrapText(true);
        label.setId("white-label");

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
        borderPane.setId("background-ui");
        borderPane.setTop(label);
        borderPane.setCenter(hBox);

        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());

        stage.setResizable(false);
        stage.setTitle("Singleplayer");
        stage.setWidth(300);
        stage.setHeight(120);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Opens a web browser and downloads the latest WG-Singleplayer-Files.
     *
     * @since 1.1.0
     */
    private void download() {
        try {
            URL url = new URL("https://github.com/GregorGott/WG-Singleplayer-Files/releases/download/release/WG-Singleplayer-Files.zip");

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(url.toURI());
            }

            chooseFile();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
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