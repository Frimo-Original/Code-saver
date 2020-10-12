package com.frimo.codesaver.controlls;

import java.io.File;

public class FileView extends File {
    public FileView(String pathname) {
        super(pathname);
    }

    @Override
    public String toString() {
        return getName();
    }
}