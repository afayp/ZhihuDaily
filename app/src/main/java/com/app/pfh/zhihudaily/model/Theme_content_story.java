package com.app.pfh.zhihudaily.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/2/4.
 */
public class Theme_content_story implements Serializable {

    private String title;
    private List<String> images;
    private int id;
    private int type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Theme_content_story{" +
                "title='" + title + '\'' +
                ", images=" + images +
                ", id=" + id +
                ", type=" + type +
                '}';
    }
}
