package com.dania.one.Model;

public class MsgModel {

    private String type;
    private String time;
    private String msg_text;
    private String direction;
    private String uri;
    private String date;
    private String timestamp;
    private String key;
    private String extra;

    public MsgModel(String type, String time, String msg_text, String direction, String uri, String date, String timestamp, String key, String extra) {
        this.type = type;
        this.time = time;
        this.msg_text = msg_text;
        this.direction = direction;
        this.uri = uri;
        this.date = date;
        this.timestamp = timestamp;
        this.key = key;
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg_text() {
        return msg_text;
    }

    public void setMsg_text(String msg_text) {
        this.msg_text = msg_text;
    }
}
