package com.frimo.codesaver.controlls;

import com.frimo.codesaver.FileManager;
import com.frimo.codesaver.controllers.MainController;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class AppTreeView extends TreeView<FileView> {
    private TreeItem<FileView> rootTreeItem;

    public AppTreeView() {
        setShowRoot(false);
    }

    public void setPathProject(String pathProject) {
        rootTreeItem = new TreeItem<>(new FileView(pathProject));
        setRoot(rootTreeItem);
    }

    public void update() {
        update(rootTreeItem);
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
        try { //ConcurrentModificationException
            for (TreeItem<FileView> i : item.getChildren()) {
                FileView file = i.getValue();

                if (!file.exists())
                    item.getChildren().remove(i);
            }
        }
        catch (Exception exp) {}
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
            if (file.getParent().equals(rootTreeItem.getValue().getPath()))
                item.setGraphic(new ImageView(new Image(MainController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/Project.png"))));
            else
                item.setGraphic(new ImageView(new Image(MainController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/Folder.png"))));

            update(item);
        } else
            switch (FileManager.getFileExtension(file.getName())) {
                case "java":
                    item.setGraphic(new ImageView(new Image(MainController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/Java.png"))));
                    break;
                case "png":
                    item.setGraphic(new ImageView(new Image(MainController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/Picture.png"))));
                    break;
                default:
                    item.setGraphic(new ImageView(new Image(MainController.class.getClassLoader().getResourceAsStream("com/frimo/codesaver/res/File.png"))));
            }

        root.getChildren().add(item);
    }
}

/*class FileView extends File {
    public FileView(String pathname) {
        super(pathname);
    }

    @Override
    public String toString() {
        return getName();
    }
}*/