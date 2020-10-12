package com.frimo.codesaver.stages;

import com.frimo.codesaver.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MessageStage
{
    @FXML
    private Label label;
    @FXML
    private Button ok;

    private Stage stage = new Stage();

    public MessageStage(Window window, String message) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/message.fxml"));
        loader.setController(this);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(window);
        stage.setTitle("Message");
        stage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/CodeSaver.png")));

        try {
            stage.setScene(new Scene(loader.load()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        label.setText(message);
    }

    public void setMessage(String message) {
        label.setText(message);
    }

    @FXML
    private void pressOk() {
        stage.close();
    }

    public void show() {
        stage.showAndWait();
    }
}