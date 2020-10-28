package com.frimo.codesaver.controllers;

import com.frimo.codesaver.filemanager.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.File;

public class CodeController extends Stage
{
    @FXML
    private SplitPane splitPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TreeView<FileView> treeView;

    @FXML
    private TabPane tabPane;

    private TreeItem<FileView> rootItem;
    private Actions actions;

    private int countNotSave = 0;

    private final String CHANGE_SYMBOL = "*";

    public CodeController(Actions actions, String root) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("com/frimo/codesaver/fxml/code.fxml"));
        loader.setController(this);
        setScene(new Scene(loader.load()));
        setTitle("Code");
        getScene().heightProperty().addListener((observableValue, number, t1) -> splitPane.setMinHeight((double) t1 - menuBar.getHeight()));

        setOnCloseRequest(ev -> {
            for (Tab tab : tabPane.getTabs())
                if (checkChangesTab(tab)) {
                    try {
                        switch (new AnswerController(CodeController.this, "Save changes?").getResult()) {
                            case AnswerController.RESULT_OK:
                                pressSaveAll();
                                break;
                            case AnswerController.RESULT_CANCEL:
                                ev.consume();
                        }
                    } catch (Exception e) { }

                    break;
                }
        });

        this.actions = actions;

        rootItem = new TreeItem<FileView>(new FileView(root));
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);

        ContextMenu contextMenu = new ContextMenu();

        treeView.setOnMouseClicked(e ->
        {
            contextMenu.hide();

            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                FileView fileView = treeView.getSelectionModel().getSelectedItem().getValue();

                if (fileView.isFile())
                    actions.pressFileOpen(new File(fileView.getPath()));
            }
            else if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1)
            {
                FileView fileView = treeView.getSelectionModel().getSelectedItem().getValue();
                contextMenu.getItems().clear();

                if (fileView.isFile()) {
                    MenuItem menuOpen = new MenuItem("Open file");
                    menuOpen.setOnAction(actionEvent -> actions.pressFileOpen(new File(fileView.getPath())));
                    MenuItem menuDelete = new MenuItem("Delete file");
                    menuDelete.setOnAction(actionEvent -> actions.pressFileDelete(new File(fileView.getPath())));
                    contextMenu.getItems().addAll(menuOpen, menuDelete);
                }
                else if (fileView.isDirectory())
                {
                    MenuItem menuNewFile = new MenuItem("New file");
                    menuNewFile.setOnAction(actionEvent -> actions.pressNewFile(fileView.getPath()));

                    MenuItem menuNewFolder = new MenuItem("New folder");
                    menuNewFolder.setOnAction(actionEvent -> actions.pressNewFolder(fileView.getPath()));

                    MenuItem menuDelete = new MenuItem("Delete");
                    menuDelete.setOnAction(actionEvent -> actions.pressFileDelete(new File(fileView.getPath())));

                    contextMenu.getItems().addAll(menuNewFile, menuNewFolder, menuDelete);
                }

                MenuItem menuExport = new MenuItem("Export");
                menuExport.setOnAction(actionEvent -> actions.pressExport(fileView));
                contextMenu.getItems().add(menuExport);

                contextMenu.show(treeView, e.getScreenX(), e.getScreenY());
            }
        });
    }

    public interface Actions {
        void pressNewProject();
        void pressNewFile(String path);
        void pressNewFolder(String path);
        void pressFileOpen(File file);
        void pressFileDelete(File file);
        void pressSave(Tab tab);
        void pressExport(File fileDir);
    }

    public void addOpenFile(File file, String text)
    {
        Tab tab = searchTab(file.getPath());

        if (searchTab(file.getPath()) == null) {
            Tab newTab = new Tab(file.getName());
            newTab.setId(file.getPath());
            TextArea textArea = new TextArea();
            textArea.setStyle("-fx-font-size: 15px;");
            newTab.setContent(textArea);
            textArea.setText(text);

            textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!checkChangesTab(newTab)) {
                    countNotSave++;
                    addChangeSymbol(newTab);
                }
            });

            newTab.setOnCloseRequest(ev -> {
                try {
                    if (checkChangesTab(newTab)) {
                        switch (new AnswerController(CodeController.this, "Save changes?").getResult()) {
                            case AnswerController.RESULT_OK:
                                actions.pressSave(newTab);
                            case AnswerController.RESULT_NO:
                                tabPane.getTabs().remove(newTab);
                                break;
                            default:
                                ev.consume();
                        }
                    }
                }
                catch (Exception e) {e.printStackTrace();}
            });

            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
        }
        else
            selectTab(tab);
    }

    public void removeOpenFile(File file) {
        tabPane.getTabs().remove(searchTab(file.getPath()));
    }

    private Tab searchTab(String id) {
        for (Tab tab : tabPane.getTabs())
            if (tab.getId().equals(id))
                return tab;

        return null;
    }

    private void selectTab(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    private void pressNewProject() {
        actions.pressNewProject();
    }

    @FXML
    private void pressOpenSettings() {
        System.out.println("Settings");
    }

    @FXML
    private void pressSave() {
        saveTab(tabPane.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void pressSaveAll() {
        for (Tab tab : tabPane.getTabs())
            saveTab(tab);
    }

    @FXML
    private void exportCode() {
        actions.pressExport(new File(rootItem.getValue().getAbsolutePath()));  //rootItem.getValue() - dir with projects
    }

    private boolean checkChangesTab(Tab tab) {
        return tab.getText().endsWith(CHANGE_SYMBOL);
    }

    private void cleanChangeSymbol(Tab tab) {
        tab.setText(tab.getText().substring(0, tab.getText().length() - 1));
    }

    private void addChangeSymbol(Tab tab) {
        tab.setText(tab.getText() + "*");
    }

    private void saveTab(Tab tab) {
        if (checkChangesTab(tab)) {
            cleanChangeSymbol(tab);
            actions.pressSave(tab);
        }
    }

    public void update() {
        update(rootItem);
    }

    private void update(TreeItem<FileView> treeItem) {
        clean(treeItem);

        for (File i : treeItem.getValue().listFiles())
            if (!searchFileItem(treeItem, i.getPath()))
                addTreeItem(treeItem, new FileView(i.getPath()));

        for (TreeItem<FileView> i : treeItem.getChildren())
            if (i.getValue().isDirectory())
                update(i);
    }

    private void clean(TreeItem<FileView> item) {
        for (TreeItem<FileView> i : item.getChildren()) {
            FileView file = i.getValue();

            if (!file.exists())
                item.getChildren().remove(i);
        }
    }

    private boolean searchFileItem(TreeItem<FileView> root, String path) {
        for (TreeItem<FileView> i : root.getChildren())
            if (i.getValue().getPath().equals(path))
                return true;

        return false;
    }

    private void addTreeItem(TreeItem<FileView> root, FileView file) {
        TreeItem<FileView> item = new TreeItem<FileView>(file);

        if (file.isDirectory()) {
            if (file.getParent().equals(rootItem.getValue().getPath()))
                item.setGraphic(new ImageView(new Image(CodeController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/Project.png"))));
            else
                item.setGraphic(new ImageView(new Image(CodeController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/Folder.png"))));

            update(item);
        } else
            switch (FileManager.getFileExtension(file.getName())) {
                case "java":
                    item.setGraphic(new ImageView(new Image(CodeController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/Java.png"))));
                    break;
                case "png":
                    item.setGraphic(new ImageView(new Image(CodeController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/Picture.png"))));
                    break;
                default:
                    item.setGraphic(new ImageView(new Image(CodeController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/File.png"))));
            }

        root.getChildren().add(item);
    }
}

class FileView extends File {
    public FileView(String pathname) {
        super(pathname);
    }

    @Override
    public String toString() {
        return getName();
    }
}