package com.dania.one.Model;

public class CategoriesPublicModel {
    private String name;
    private String uri;
    Boolean isSelected = false;



    public CategoriesPublicModel(String name, String uri, Boolean isSelected) {
        this.name = name;
        this.uri = uri;
        this.isSelected = isSelected;
    }

    public CategoriesPublicModel(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public CategoriesPublicModel() {
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
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
}
