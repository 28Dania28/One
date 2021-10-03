package com.dania.one.Model;

public class MyData {
    private String display_picture;
    private String email_id;
    private String family_name;
    private String first_name;
    private String name;
    private String user_id;

    public MyData() {
    }

    public MyData(String display_picture, String email_id, String family_name, String first_name, String name, String user_id) {
        this.display_picture = display_picture;
        this.email_id = email_id;
        this.family_name = family_name;
        this.first_name = first_name;
        this.name = name;
        this.user_id = user_id;
    }

    public String getDisplay_picture() {
        return display_picture;
    }

    public void setDisplay_picture(String display_picture) {
        this.display_picture = display_picture;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
