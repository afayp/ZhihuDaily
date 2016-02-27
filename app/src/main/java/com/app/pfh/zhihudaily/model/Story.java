package com.app.pfh.zhihudaily.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;


public class Story implements Serializable {
    /**
     * stories : 当日新闻
     title : 新闻标题
     images : 图像地址（官方 API 使用数组形式。目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
     ga_prefix : 供 Google Analytics 使用
     type : 作用未知
     id : url 与 share_url 中最后的数字（应为内容的 id）
     multipic : 消息是否包含多张图片（仅出现在包含多图的新闻中）
     */
    private String title;
    private String ga_prefix;
    private List<String> images;
    private int id;
    private int type;


    public Story() {
    }

    public Story(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
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
        return "Story{" +
                "title='" + title + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", images=" + images +
                ", id=" + id +
                ", type=" + type +
                '}';
    }

}
