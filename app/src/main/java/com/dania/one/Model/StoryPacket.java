package com.dania.one.Model;

import java.util.ArrayList;

public class StoryPacket {


    private UserModel userModel;
    private ArrayList<Story> storyModels;

    public StoryPacket(UserModel userModel, ArrayList<Story> storyModels) {
        this.userModel = userModel;
        this.storyModels = storyModels;
    }

    public StoryPacket() {
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public ArrayList<Story> getStoryModels() {
        return storyModels;
    }

    public void setStoryModels(ArrayList<Story> storyModels) {
        this.storyModels = storyModels;
    }
}
