package com.frimo.codesaver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager
{
    public FileManager() { }

    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else
            return "";
    }

    public void createDir(String path) throws Exception {
        File dir = new File(path);

        if (!dir.exists() && !dir.mkdir())
            throw new Exception("Directory not created");
    }

    public void createFile(String path) throws Exception {
        File file = new File(path);

        if (!file.exists())
            file.createNewFile();
    }

    public void write(String path, String text) throws IOException {
        FileWriter writer = new FileWriter(new File(path));
        writer.write(text);
        writer.close();
    }

    public String read(File file) throws IOException {
        String readText = "";

        FileReader reader = new FileReader(file);
        int i;

        while ((i = reader.read()) != -1)
            readText += Character.toString((char) i);

        reader.close();

        return readText;
    }

    public void delete(String path) {
        File file = new File(path);

        if (file.isDirectory())
            for (File i : file.listFiles())
                delete(i.getPath());

        file.delete();
    }
}