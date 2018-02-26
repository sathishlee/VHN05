package com.unicef.vhn.model;

/**
 * Created by Suthishan on 20/1/2018.
 */

public class NotificationModel {
    private String title, genre, year;

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    private  int img_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public NotificationModel(String title, String genre, String year, int img_id) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.img_id = img_id;
    }
}
