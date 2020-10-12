package com.frimo.codesaver.controlls;

import com.frimo.codesaver.FileManager;
import com.frimo.codesaver.stages.AnswerStage;
import com.frimo.codesaver.stages.MessageStage;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;

public class AppTabPane extends TabPane
{
    private final String CHANGE_SYMBOL = "*";

    private FileManager fileManager;

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void addTab(String path) throws IOException {
        for (Tab tab : getTabs())
            if (tab.getId().equals(path))
                return;

        Tab tab = new Tab(new File(path).getName());
        TextArea textArea = new TextArea();
        textArea.setText(fileManager.read(new File(path)));
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!checkChangesTab(tab))
                addChangeSymbol(tab);
        });

        tab.setOnCloseRequest(ev -> {
            if (checkChangesTab(tab)) {
                AnswerStage answerStage = new AnswerStage(getScene().getWindow(), "Save changes?");
                answerStage.show();
                try {
                    switch (answerStage.getData()) {
                        case AnswerStage.OK:
                            fileManager.write(tab.getId(), ((TextArea) tab.getContent()).getText());
                            break;
                        case AnswerStage.CANCEL:
                            ev.consume();
                        default:
                            break;
                    }
                }
                catch (IOException e) {
                    MessageStage messageStage = new MessageStage(getScene().getWindow(), "Error save file");
                    messageStage.show();
                }
            }
        });

        tab.setContent(textArea);
        tab.setId(path);
        getTabs().add(tab);
    }

    public void saveCurrentTab() throws IOException {
        Tab tab = getSelectionModel().getSelectedItem();

        if (tab == null)
            return;

        fileManager.write(tab.getId(), ((TextArea) tab.getContent()).getText());
        deleteChangeSymbol(tab);
    }

    public void saveAll() throws IOException {
        for (Tab tab : getTabs())
            if (checkChangesTab(tab)) {
                fileManager.write(tab.getId(), ((TextArea) tab.getContent()).getText());
                deleteChangeSymbol(tab);
            }
    }

    public boolean checkNotSaved() {
        for (Tab tab : getTabs())
            if (checkChangesTab(tab))
                return true;
        return false;
    }

    private boolean checkChangesTab(Tab tab) {
        return tab.getText().endsWith(CHANGE_SYMBOL);
    }

    private void addChangeSymbol(Tab tab) {
        tab.setText(tab.getText() + CHANGE_SYMBOL);
    }

    private void deleteChangeSymbol(Tab tab) {
        tab.setText(tab.getText().substring(0, tab.getText().length() - 1));
    }
}