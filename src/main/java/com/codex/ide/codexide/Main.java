package com.codex.ide.codexide;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        Scene scene = new Scene(fxmlLoader.load(), 1080 ,800);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());


//        scene.getStylesheets().add(getClass().getResource("/home/rohit/Desktop/Work/my-code-base/only java/codex-ide/css/style.css").toExternalForm());

        stage.setTitle("CODEx!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}