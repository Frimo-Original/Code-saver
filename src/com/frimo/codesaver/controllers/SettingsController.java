package com.frimo.codesaver.controllers;

import com.frimo.codesaver.controlls.FileView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;

import java.io.IOException;


public class SettingsController
{
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TreeView<ItemSettings> treeView;

    private TreeItem<ItemSettings> treeItemRoot = new TreeItem<>();

    public SettingsController() {

    }

    @FXML
    private void initialize()
    {
        treeView.setRoot(treeItemRoot);
        treeView.setShowRoot(false);

        FXMLLoader loader = new FXMLLoader(SettingsController.class.getResource("../fxml/settings/path.fxml"));
        //loader.setController();

        TreeItem<ItemSettings> treeItem = new TreeItem<>();

        try {
            ItemSettings itemSettings = new ItemSettings(loader.load(), "Path");
            treeItem.setValue(itemSettings);
        }
        catch (IOException exp) {
            exp.printStackTrace();
        }

        treeItemRoot.getChildren().add(treeItem);

        treeView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                TreeItem<ItemSettings> item = treeView.getSelectionModel().getSelectedItem();
                scrollPane.setContent(item.getValue().getNode());
            }
        });
    }

    /*@FXML
    private void pressProjectBrowse() {
        System.out.println("Hello");
    }*/

    @FXML
    private void pressCancel() {
        getWindow().hide();
    }

    private Window getWindow() {
        return treeView.getScene().getWindow();
    }
}

class ItemSettings {
    private Node node;
    private String name;

    public ItemSettings(Node node, String name) {
        this.node = node;
        this.name = name;
    }

    public Node getNode() {
        return node;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}