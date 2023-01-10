package com.example.imgproc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 600);

        ImageServiceInjector imageServiceInjector = new ImageServiceInjectorImpl();
        imageServiceInjector.setServices(fxmlLoader.getController());

        stage.setTitle("MPZIP-11 Lab: Image processing");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}