package com.app.pfh.zhihudaily.model;

import java.util.List;

/**
 * Created by Administrator on 2016/2/2.
 */
public class Latest {

    private String date;
    private List<Story> stories;
    private List<TopSotory> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<TopSotory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopSotory> top_stories) {
        this.top_stories = top_stories;
    }
}
