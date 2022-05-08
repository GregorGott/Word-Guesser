module com.gregorgott.guesser {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.eclipse.jgit;

    opens com.gregorgott.guesser to javafx.fxml;
    exports com.gregorgott.guesser;
    exports com.gregorgott.guesser.panes;
    opens com.gregorgott.guesser.panes to javafx.fxml;
}