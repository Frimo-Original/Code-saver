package com.frimo.codesaver.controllers;

import com.frimo.codesaver.FileManager;
import com.frimo.codesaver.Main;
import com.frimo.codesaver.Settings;
import com.frimo.codesaver.ZipUtility;
import com.frimo.codesaver.controlls.AppTabPane;
import com.frimo.codesaver.controlls.AppTreeView;
import com.frimo.codesaver.controlls.FileView;
import com.frimo.codesaver.stages.AnswerStage;
import com.frimo.codesaver.stages.DataStage;
import com.frimo.codesaver.stages.MessageStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class MainController
{
    @FXML
    private SplitPane splitPane;
    @FXML
    private AppTreeView treeView;
    @FXML
    private AppTabPane tabPane;

    private final Settings settings;
    private final FileManager fileManager = new FileManager();
    private final ZipUtility zipUtility = new ZipUtility();

    public MainController(Settings settings) {
        this.settings = settings;
    }

    @FXML
    public void initialize()
    {
        tabPane.setFileManager(fileManager);
        treeView.setPathProject(settings.getPathProjects());

        ContextMenu contextMenu = new ContextMenu();

        treeView.setOnMouseClicked(e ->
        {
            contextMenu.hide();

            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                TreeItem<FileView> item = treeView.getSelectionModel().getSelectedItem();

                if (item == null)
                    return;

                FileView fileView = item.getValue();

                if (fileView.isFile())
                    openFile(fileView.getPath());
            }
            else if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1)
            {
                FileView fileView = treeView.getSelectionModel().getSelectedItem().getValue();
                contextMenu.getItems().clear();

                if (fileView.isFile()) {
                    MenuItem menuOpen = new MenuItem("Open file");
                    menuOpen.setOnAction(actionEvent -> openFile(fileView.getPath()));
                    contextMenu.getItems().addAll(menuOpen);
                }
                else if (fileView.isDirectory())
                {
                    MenuItem menuNewFile = new MenuItem("New file");
                    menuNewFile.setOnAction(actionEvent -> {
                        DataStage dataStage = new DataStage(getParent(), "Enter file name: ");
                        dataStage.show();
                        if (dataStage.getData() != null)
                            try {
                                fileManager.createFile(fileView.getPath() + File.separator + dataStage.getData());
                            }
                            catch (Exception exp) {}

                        treeView.update();
                    });

                    MenuItem menuNewFolder = new MenuItem("New folder");
                    menuNewFolder.setOnAction(actionEvent -> {
                        DataStage dataStage = new DataStage(getParent(), "Enter folder name: ");
                        dataStage.show();
                        if (dataStage.getData() != null)
                            try {
                                fileManager.createDir(fileView.getPath() + File.separator + dataStage.getData());
                            }
                            catch (Exception exp) {}

                        treeView.update();
                    });

                    contextMenu.getItems().addAll(menuNewFile, menuNewFolder);
                }

                MenuItem menuDelete = new MenuItem("Delete");
                menuDelete.setOnAction(actionEvent -> {
                    AnswerStage answerStage = new AnswerStage(getParent(), "Confirm action");
                    answerStage.show();
                    if (answerStage.getData() == AnswerStage.OK)
                        fileManager.delete(fileView.getPath());

                    treeView.update();
                });

                MenuItem menuExport = new MenuItem("Export");
                menuExport.setOnAction(actionEvent -> exportCode(fileView));

                contextMenu.getItems().addAll(menuExport, menuDelete);

                contextMenu.show(treeView, e.getScreenX(), e.getScreenY());
            }
        });

        treeView.update();
    }

    private void openFile(String path) {
        try {
            tabPane.addTab(path);
        }
        catch (IOException exp) {
            MessageStage messageStage = new MessageStage(getParent(), "Error reading file");
            messageStage.show();
        }
    }

    private Window getParent() {
        return splitPane.getScene().getWindow();
    }

    private void exportCode(File file) {
        MessageStage messageStage = new MessageStage(getParent(), "Successfully!");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(settings.getPathExports()));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip archive", "*.zip"));
        File fileZip = fileChooser.showSaveDialog(getParent());

        if (fileZip == null)
            return;

        try {
            zipUtility.zip(file, fileZip);
        }
        catch (IOException exp) {
            messageStage.setMessage("Error export file");
        }

        messageStage.show();
    }

    @FXML
    private void pressNewProject() {
        DataStage dataStage = new DataStage(getParent(),"Project name: ");
        dataStage.show();

        try {
            if (dataStage.getData() != null)
                fileManager.createDir(settings.getPathProjects() + File.separator + dataStage.getData());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        treeView.update();
    }

    @FXML
    private void exportCode() {
        exportCode(new File(settings.getPathProjects()));
    }

    @FXML
    private void pressOpenSettings() {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/settings.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(getParent());
        stage.setTitle("Settings");
        stage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/CodeSaver.png")));

        try {
            stage.setScene(new Scene(loader.load()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        stage.show();*/
    }

    @FXML
    private void pressSave() {
        try {
            tabPane.saveCurrentTab();
        }
        catch (IOException exp) {
            MessageStage messageStage = new MessageStage(getParent(), "Error save file");
            messageStage.show();
        }
    }

    @FXML
    private void pressSaveAll() {
        try {
            tabPane.saveAll();
        }
        catch (IOException exp) {
            MessageStage messageStage = new MessageStage(getParent(), "Error save file");
            messageStage.show();
        }
    }
}