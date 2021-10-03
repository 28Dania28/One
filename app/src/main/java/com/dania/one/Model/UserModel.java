package com.dania.one.Model;

public class UserModel {

    private String id;
    private String Name;
    private String DisplayPicture;
    private String EmailId;
    private String today_dp;
    private String status;
    public UserModel(String id, String Name, String DisplayPicture, String EmailId, String today_dp, String status ) {
        this.id = id;
        this.Name = Name;
        this.DisplayPicture = DisplayPicture;
        this.EmailId = EmailId;
        this.today_dp = today_dp;
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToday_dp() {
        return today_dp;
    }

    public void setToday_dp(String today_dp) {
        this.today_dp = today_dp;
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

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }
}
