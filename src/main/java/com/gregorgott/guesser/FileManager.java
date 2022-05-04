package com.gregorgott.guesser;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Manages all file actions.
 *
 * @author GregorGott
 * @version 1.0.0
 * @since 2022-06-22
 */
public class FileManager {

    /**
     * Show a file chooser which only accepts text files and get the selected file.
     *
     * @return The selected text file.
     */
    public File selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Singleplayer file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        return new File(fileChooser.showOpenDialog(new Stage()).getPath());
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