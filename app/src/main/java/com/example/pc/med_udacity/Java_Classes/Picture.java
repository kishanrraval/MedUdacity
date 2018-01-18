package com.example.pc.med_udacity.Java_Classes;

public class Picture
{
    private String id;
    private String path;
    private String date;

    public Picture(String id,String path, String date)
    {
        this.id = id;
        this.date = date;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
