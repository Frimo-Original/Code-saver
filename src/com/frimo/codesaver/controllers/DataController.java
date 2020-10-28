package com.frimo.codesaver.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DataController extends Stage
{
    private String data;

    @FXML
    private Label label;

    @FXML
    private TextField textField;

    public DataController(Window parent, String text) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(getClass().getResource("../fxml/data.fxml"));
        loader.setLocation(getClass().getClassLoader().getResource("com/frimo/codesaver/fxml/data.fxml"));
        loader.setController(this);
        setScene(new Scene(loader.load()));
        setResizable(false);
        setTitle("Enter data");
        initModality(Modality.WINDOW_MODAL);
        initOwner(parent);

        label.setText(text);

        showAndWait();
    }

    public String getData() {
        return data;
    }

    @FXML
    private void pressOk() {
        String data = textField.getText();

        if (!data.equals("") && !data.trim().equals(""))
            this.data = data.trim();

        close();
    }

    @FXML
    private void pressCancel() {
        close();
    }
}