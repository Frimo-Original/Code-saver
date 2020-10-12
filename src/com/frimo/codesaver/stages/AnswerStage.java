package com.frimo.codesaver.stages;

import com.frimo.codesaver.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class AnswerStage
{
    public static final int OK = 1;
    public static final int NO = 0;
    public static final int CANCEL = -1;

    @FXML
    private Label label;

    private Stage stage = new Stage();
    private int data = CANCEL;

    public AnswerStage(Window parent, String dataText) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/answer.fxml"));
        loader.setController(this);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parent);
        stage.setTitle("Confirm");
        stage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/CodeSaver.png")));

        try {
            stage.setScene(new Scene(loader.load()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        label.setText(dataText);
    }

    @FXML
    private void pressOk() {
        data = OK;
        stage.close();
    }

    @FXML
    private void pressNo() {
        data = NO;
        stage.close();
    }

    @FXML
    private void pressCancel() {
        data = CANCEL;
        stage.close();
    }

    public void show() {
        stage.showAndWait();
    }

    public int getData() {
        return data;
    }
}