package com.app.pfh.zhihudaily.model;


import java.util.List;

import io.realm.RealmObject;

public class ShouCangModel extends RealmObject{

    private String title;
    private String image;
    private int id;
    private boolean isLatest;

    public ShouCangModel() {
    }

    public ShouCangModel(String title, String image, int id, boolean isLatest) {
        this.title = title;
        this.image = image;
        this.id = id;
        this.isLatest = isLatest;
    }

    public ShouCangModel(String title, int id, boolean isLatest) {
        this.title = title;
        this.id = id;
        this.isLatest = isLatest;
    }

    public boolean isLatest() {
        return isLatest;
    }

    public void setIsLatest(boolean isLatest) {
        this.isLatest = isLatest;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
