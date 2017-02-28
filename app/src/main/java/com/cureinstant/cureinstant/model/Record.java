package com.cureinstant.cureinstant.model;

/**
 * Created by lokeshsaini94 on 27-02-2017.
 */

public class Record {
    private String file;
    private String title;
    private int icon;

    public Record(String file, String title, int icon) {
        this.file = file;
        this.title = title;
        this.icon = icon;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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
