package com.app.pfh.zhihudaily.model;


public class Theme_others {
    /**主题列表里的其他条目
     * color : 颜色，作用未知
     thumbnail : 供显示的图片地址
     description : 主题日报的介绍
     id : 该主题日报的编号
     name : 供显示的主题日报名称
     */

    private int color;
    private String thumbnail;
    private String description;
    private int id;
    private String name;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
