package com.example.cryptofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class CryptoFXApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CryptoFXApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);
        stage.setTitle("Log in | CryptoFX");
        Image ico = new Image("D:\\IdeaProjects\\CryptoFX\\src\\main\\java\\com\\example\\cryptofx\\img\\logo.png"); // No funciona con ruta relativa
        stage.getIcons().add(ico);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}