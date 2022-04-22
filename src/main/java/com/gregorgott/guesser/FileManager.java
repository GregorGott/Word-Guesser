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
    public File selectFile() {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setWidth(300);
        stage.setHeight(200);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Singleplayer file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        return new File (fileChooser.showOpenDialog(stage).getPath());
    }

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
