package com.dania.one.Model;

public class Story {
    private String date;
    private String story_id;
    private long views;
    private String uid;
    private int timestamp;
    private String type;
    private String uri;

    public Story(String date, String story_id, int timestamp, String type, String uid, String uri, long views) {
        this.date = date;
        this.story_id = story_id;
        this.timestamp = timestamp;
        this.type = type;
        this.uri = uri;
        this.uid = uid;
        this.views = views;
    }

    public Story() {
    }

    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
