package com.gregorgott.guesser;

import com.gregorgott.mdialogwindows.MAlert;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Manages all file actions.
 *
 * @author GregorGott
 * @version 1.1.5
 * @since 2022-06-16
 */
public class SinglePlayerFileSelector {
    private MAlert mAlert;
    private File file;

    /**
     * Shows a MAlert with the options to download the single-player files, open a file chooser and cancel.
     *
     * @return A selected text file.
     * @since 1.0.0
     */
    public File selectSingleplayerFile(Window window) {
        mAlert = new MAlert(MAlert.MAlertType.CONFIRMATION, "Singleplayer", window);
        mAlert.setMAlertStyle(MAlert.MAlertStyle.DARK_ROUNDED);
        mAlert.setHeadline("Select Singleplayer files.");
        mAlert.setContentText("Do you want to download all Singleplayer files? If you want to " +
                "select a file from your Computer, click 'Open'.");
        mAlert.addButton("Download", x -> download(), false);
        mAlert.addButton("Open", x -> chooseFile(), true);
        mAlert.addButton("Cancel", x -> mAlert.closeAlert(), false);
        mAlert.getStage().showAndWait();

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

        mAlert.closeAlert();
    }
}