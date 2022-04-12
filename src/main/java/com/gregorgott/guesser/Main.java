package com.gregorgott.guesser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Loads the main menu FXML in the Stage and shows it.
 *
 * @author GregorGott
 * @version 1.0.1
 * @since 2022-04-09
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Load main-menu-scene.fxml in 'Word Guesser' Stage
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Word Guesser");
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }
}