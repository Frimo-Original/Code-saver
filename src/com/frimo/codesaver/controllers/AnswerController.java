package com.frimo.codesaver.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AnswerController extends Stage
{
    public static final int RESULT_CANCEL = -1;
    public static final int RESULT_OK = 0;
    public static final int RESULT_NO = 1;

    private int result = RESULT_CANCEL;

    @FXML
    private Button buttonOk = new Button();

    @FXML
    private Button buttonNo = new Button();

    @FXML
    private Button buttonCancel = new Button();

    @FXML
    private Text answer = new Text();

    public AnswerController(Window parent, String textAnswer) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("com/frimo/codesaver/fxml/answer.fxml"));
        loader.setController(this);

        setWidth(250);
        setHeight(130);
        setScene(new Scene(loader.load()));
        setResizable(false);
        setTitle("Answer");
        initModality(Modality.WINDOW_MODAL);
        initOwner(parent);

        answer.setText(textAnswer);

        buttonOk.setOnAction(e -> {
            result = RESULT_OK;
            close();
        });

        buttonNo.setOnAction(e -> {
            result = RESULT_NO;
            close();
        });

        buttonCancel.setOnAction(e -> {
            result = RESULT_CANCEL;
            close();
        });

        showAndWait();
    }

    public int getResult() {
        return result;
    }
}