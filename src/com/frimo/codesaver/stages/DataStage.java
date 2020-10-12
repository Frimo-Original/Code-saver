package com.frimo.codesaver.stages;

import com.frimo.codesaver.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class DataStage
{
    @FXML
    private Label label;
    @FXML
    private TextField textField;

    private Stage stage = new Stage();
    private String data = null;

    public DataStage(Window parent, String dataText) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/data.fxml"));
        loader.setController(this);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parent);
        stage.setTitle("Enter data");
        stage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/CodeSaver.png")));

        try {
            stage.setScene(new Scene(loader.load()));
        }
        catch (IOException e) { }

        label.setText(dataText);
    }

    public void show() {
        stage.showAndWait();
    }

    public String getData() {
        return data;
    }

    @FXML
    private void pressOk() {
        data = textField.getText();
        stage.close();
    }

    @FXML
    private void pressCancel() {
        data = null;
        stage.close();
    }
}