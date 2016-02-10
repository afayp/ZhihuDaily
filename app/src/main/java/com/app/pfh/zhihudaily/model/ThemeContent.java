package com.app.pfh.zhihudaily.model;


import java.util.List;

public class ThemeContent {

    private List<Theme_content_story> stories;
    private String description;
    private String background;
    private int color;
    private String name;
    private String image;
    private List<Editor> editors;
    private String image_source;

    public List<Theme_content_story> getStories() {
        return stories;
    }

    public void setStories(List<Theme_content_story> stories) {
        this.stories = stories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Editor> getEditors() {
        return editors;
    }

    public void setEditors(List<Editor> editors) {
        this.editors = editors;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }
}
