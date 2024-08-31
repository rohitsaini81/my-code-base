module com.codex.ide.codexide {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.codex.ide.codexide to javafx.fxml;
    exports com.codex.ide.codexide;
}