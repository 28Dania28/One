package com.dania.one.Model;

public class NotificationModel {
    private String noti_type;
    private String uid;
    private String name;
    private String dp;
    private String story_id;
    private String story_type;
    private String story_uri;
    private String reaction;
    private int timestamp;

    public NotificationModel(String noti_type, String uid, String name, String dp, String story_id, String story_type, String story_uri, String reaction, int timestamp) {
        this.noti_type = noti_type;
        this.uid = uid;
        this.name = name;
        this.dp = dp;
        this.story_id = story_id;
        this.story_type = story_type;
        this.story_uri = story_uri;
        this.reaction = reaction;
        this.timestamp = timestamp;
    }

    public NotificationModel() {
    }

    public String getNoti_type() {
        return noti_type;
    }

    public void setNoti_type(String noti_type) {
        this.noti_type = noti_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public String getStory_type() {
        return story_type;
    }

    public void setStory_type(String story_type) {
        this.story_type = story_type;
    }

    public String getStory_uri() {
        return story_uri;
    }

    public void setStory_uri(String story_uri) {
        this.story_uri = story_uri;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
