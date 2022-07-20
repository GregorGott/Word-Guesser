module com.gregorgott.guesser {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires com.gregorgott.mdialogwindows;

    opens com.gregorgott.guesser to javafx.fxml;
    exports com.gregorgott.guesser;
    exports com.gregorgott.guesser.AskQuestionPanes;
    opens com.gregorgott.guesser.AskQuestionPanes to javafx.fxml;
    exports com.gregorgott.guesser.SetQuestionPanes;
    opens com.gregorgott.guesser.SetQuestionPanes to javafx.fxml;
}