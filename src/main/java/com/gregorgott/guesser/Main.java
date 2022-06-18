package com.gregorgott.guesser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Loads the main menu FXML in the Stage and shows it.
 *
 * @author GregorGott
 * @version 1.0.2
 * @since 2022-06-16
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Load main-menu-scene.fxml in 'Word Guesser' Stage
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mode-selector-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Word Guesser");
        stage.setResizable(false);

        if (System.getProperty("os.name").contains("windows")) {
            stage.getIcons().add(new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("images/app-icon_16x16.png"))));
        }

        stage.setScene(scene);
        stage.show();
    }
}