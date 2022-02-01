package com.example.campus360.ui.camera;

public class ListImage {
    private String filename;
    private String location;
    private String date;
    private String time;
    private String type;
    private String imgURL;

    public ListImage(String filename, String location, String date,  String time, String type, String imgURL) {
        this.filename = filename;
        this.location = location;
        this.date = date;
        this.type = type;
        this.imgURL = imgURL;
        this.time = time;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
