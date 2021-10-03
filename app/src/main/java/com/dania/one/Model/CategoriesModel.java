package com.dania.one.Model;

public class CategoriesModel {
    private String name;
    private String uri;
    private int type;
    Boolean isSelected = false;

    public CategoriesModel() {
    }

    public CategoriesModel(String name, String uri, int type, Boolean isSelected) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
