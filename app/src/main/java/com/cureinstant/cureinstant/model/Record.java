package com.cureinstant.cureinstant.model;

/**
 * Created by lokeshsaini94 on 27-02-2017.
 */

public class Record {
    private String filePath;
    private String title;
    private int icon;

    public Record(String filePath, String title, int icon) {
        this.filePath = filePath;
        this.title = title;
        this.icon = icon;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
