package com.dania.one.Model;

import java.util.ArrayList;

public class DateWiseStoriesModel {
    private String date;
    private ArrayList<Story> storyModels;

    public DateWiseStoriesModel() {
    }

    public DateWiseStoriesModel(String date, ArrayList<Story> storyModels) {
        this.date = date;
        this.storyModels = storyModels;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Story> getStoryModels() {
        return storyModels;
    }

    public void setStoryModels(ArrayList<Story> storyModels) {
        this.storyModels = storyModels;
    }
}
