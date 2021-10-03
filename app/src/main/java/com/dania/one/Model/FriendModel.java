package com.dania.one.Model;

public class FriendModel {
    private String id;
    private String Name;
    private String DisplayPicture;
    private String token;

    public FriendModel(String id, String name, String displayPicture, String token) {
        this.id = id;
        Name = name;
        DisplayPicture = displayPicture;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDisplayPicture() {
        return DisplayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        DisplayPicture = displayPicture;
    }
}
