package com.frimo.codesaver;

import java.io.File;

public class Settings
{
    public static final String SETTINGS_PATH_FOLDER = System.getProperty("user.home") + File.separator + ".CodeSaver";
    public static final String SETTINGS_PATH_FILE = SETTINGS_PATH_FOLDER + File.separator + "settings";

    private String pathProjects = SETTINGS_PATH_FOLDER + File.separator + "projects";  //default
    private String pathExports = SETTINGS_PATH_FOLDER + File.separator + "exports";  //default

    public Settings() {}

    public Settings(String pathProjects, String pathExports) {
        setPathProjects(pathProjects);
        setPathExports(pathExports);
    }

    public String getPathExports() {
        return pathExports;
    }

    public void setPathExports(String pathExports) {
        this.pathExports = pathExports;
    }

    public String getPathProjects() {
        return pathProjects;
    }

    public void setPathProjects(String pathProjects) {
        this.pathProjects = pathProjects;
    }
}