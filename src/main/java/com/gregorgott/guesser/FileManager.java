package com.gregorgott.guesser;

import com.gregorgott.mdialogwindows.MAlert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

/**
 * Manages all file actions.
 *
 * @author GregorGott
 * @version 1.1.4
 * @since 2022-05-17
 */
public class FileManager {
    private Stage stage;
    private File file;

    public FileManager() {
        stage = new Stage();
    }

    /**
     * Shows a MAlert with the options to download the single-player files, open a file chooser and cancel.
     *
     * @return A selected text file.
     * @since 1.0.0
     */
    public File selectSingleplayerFile(Window window) {
        MAlert mAlert = new MAlert(MAlert.MAlertType.CONFIRMATION, "Singleplayer", window);
        mAlert.setAlertStyle(MAlert.MAlertStyle.DARK_ROUNDED);
        mAlert.setHeadline("Select Singleplayer files.");
        mAlert.setContentText("Do you want to download all Singleplayer files? If you want to " +
                "select a file from your Computer, click 'Open'.");
        mAlert.addButton("Download", x -> download(), false);
        mAlert.addButton("Open", x -> chooseFile(), true);
        mAlert.addButton("Cancel", x -> mAlert.closeAlert(), false);

        stage = mAlert.getStage();
        stage.showAndWait();

        return file;
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

        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            file = file.getAbsoluteFile();
        }

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