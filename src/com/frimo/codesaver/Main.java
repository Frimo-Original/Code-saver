package com.frimo.codesaver;

import com.frimo.codesaver.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Settings settings = new Settings();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        fxmlLoader.setController(new MainController(settings));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("CodeSaver");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/CodeSaver.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
