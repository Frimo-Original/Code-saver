package com.frimo.codesaver.filemanager;

import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager
{
    public static final File CACHE_FOLDER = new File(System.getProperty("user.home") + File.separator + ".CodeSaver");
    private final File SETTINGS = new File(CACHE_FOLDER.getPath() + File.separator + "settings");

    public static final String DEFAULT_PATH_CODE = FileManager.CACHE_FOLDER.getAbsolutePath() + File.separator + "projects";
    public static final String DEFAULT_PATH_EXPORT = FileManager.CACHE_FOLDER.getAbsolutePath() + File.separator + "exports";

    private final String JSON_KEY = "path-save";

    private File code;  //projects

    private final int UNKNOWN_ERROR = -1;
    private final int SUCCESSFULLY = 0;
    private final int SAME_NAME = 1;
    private final int NOT_CREATED = 2;

    public FileManager() throws IOException
    {
        if (!CACHE_FOLDER.exists() || CACHE_FOLDER.isFile())
            CACHE_FOLDER.mkdir();

        if (!SETTINGS.exists() || SETTINGS.isDirectory())
            SETTINGS.createNewFile();
        else {
            try {
                JSONObject jsonObject = new JSONObject(readFile(SETTINGS));
                code = new File(jsonObject.getString(JSON_KEY));

                if (!code.exists() || code.isFile())
                    code = null;
            }
            catch (Exception exp) {
                save(SETTINGS, "");
            }
        }

        File fileCode = new File(DEFAULT_PATH_CODE);
        File fileExport = new File(DEFAULT_PATH_EXPORT);

        if (!fileCode.exists() || fileCode.isFile())
            fileCode.mkdir();

        if (!fileExport.exists() || fileExport.isFile())
            fileExport.mkdir();
    }

    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else
            return "";
    }

    public boolean isExistsCodeDir() {
        if (code == null)
            return false;
        return true;
    }

    public void setCode(String path) {
        code = new File(path);
        StringBuffer stringBuffer = new StringBuffer();
        JSONWriter jsonWriter = new JSONWriter(stringBuffer);
        jsonWriter.object();
        jsonWriter.key(JSON_KEY);
        jsonWriter.value(path);
        jsonWriter.endObject();
        save(SETTINGS, stringBuffer.toString());
    }

    public File getCode() {
        return code;
    }

    public int createProject(String nameProject) {
        return createDir(nameProject, code.getAbsolutePath());
    }

    public int createFolder(String nameFolder, String path) {
        return createDir(nameFolder, path);
    }

    private int createDir(String nameDir, String path) {
        File dir = new File(path + File.separator + nameDir);

        if (dir.exists())
            return SAME_NAME;

        if (!dir.mkdir())
            return NOT_CREATED;
        else
            return SUCCESSFULLY;
    }

    public int createFile(String nameFile, String path) {
        File file = new File(path + File.separator + nameFile);

        if (file.exists())
            return SAME_NAME;

        try {
            if (file.createNewFile())
                return SUCCESSFULLY;
            else
                return NOT_CREATED;
        }
        catch (IOException exp) {
            return UNKNOWN_ERROR;
        }
    }

    public int save(File file, String text) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.close();
        }
        catch (IOException exp) {
            return UNKNOWN_ERROR;
        }

        return SUCCESSFULLY;
    }

    public String readFile(File file) {
        String readText = "";

        try {
            FileReader reader = new FileReader(file);
            int i;

            while ((i = reader.read()) != -1)
                readText += Character.toString((char) i);

            reader.close();
        }
        catch (IOException exp) {
            exp.printStackTrace();
        }

        return readText;
    }

    public void delete(File file) {
        if (file.isDirectory())
            for (File i : file.listFiles())
                delete(i);

        file.delete();
    }
}