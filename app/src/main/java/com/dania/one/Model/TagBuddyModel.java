package com.dania.one.Model;

public class TagBuddyModel {
    String id;
    String Name;
    String DisplayPicture;
    Boolean isSelected = false;

    public TagBuddyModel(String id, String name, String displayPicture, Boolean isSelected) {
        this.id = id;
        Name = name;
        DisplayPicture = displayPicture;
        this.isSelected = isSelected;
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

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
