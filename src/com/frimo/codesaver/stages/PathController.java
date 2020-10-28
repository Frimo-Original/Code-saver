package com.frimo.codesaver.stages;

import com.frimo.codesaver.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class PathController extends Stage
{
    @FXML
    private Label pathLabel;

    @FXML
    private TextField textField;

    @FXML
    private Button buttonOk;

    @FXML
    private CheckBox checkBox;

    private TypesPath typePath;
    private String path;
    private String defaultPath;

    public enum TypesPath {
        TYPE_FILE, TYPE_DIRECTORY
    }

    public PathController(Window parent, String textPath, TypesPath typePath, String defaultPath) throws Exception
    {
        this.typePath = typePath;
        this.defaultPath = defaultPath;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/path.fxml"));
        loader.setController(this);

        setScene(new Scene(loader.load()));
        setResizable(false);
        setTitle("Enter path");
        getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/CodeSaver.png")));
        initModality(Modality.WINDOW_MODAL);
        initOwner(parent);

        pathLabel.setText(textPath);
        checkBox.setSelected(true);
        textField.setText(defaultPath);
        textField.setDisable(true);

        checkBox.setOnAction(ev -> {
            if (checkBox.isSelected()) {
                textField.setDisable(true);
                textField.setText(defaultPath);
            } else
                textField.setDisable(false);
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(newValue);

            if (file.exists()) {
                if ((typePath == TypesPath.TYPE_DIRECTORY && file.isDirectory())
                        || (typePath == TypesPath.TYPE_FILE && file.isFile()))
                    buttonOk.setDisable(false);
//                else if (typePath == TypesPath.TYPE_FILE && file.isFile())
//                    buttonOk.setDisable(false);
                else
                    buttonOk.setDisable(true);
            } else
                buttonOk.setDisable(true);
        });

        showAndWait();
    }

    public String getPath() {
        return path;
    }

    @FXML
    private void pressCancel() {
        path = null;
        close();
    }

    @FXML
    private void pressOk() {
        path = textField.getText();
        close();
    }

    @FXML
    private void browse() {
        File file = new DirectoryChooser().showDialog(this);

        if (file != null) {
            textField.setText(file.getAbsolutePath());
            checkBox.setSelected(false);
            textField.setDisable(false);
        }
    }
}