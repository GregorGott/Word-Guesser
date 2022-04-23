module com.gregorgott.guesser {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.gregorgott.guesser to javafx.fxml;
    exports com.gregorgott.guesser;
}