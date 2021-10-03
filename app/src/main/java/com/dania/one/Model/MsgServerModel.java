package com.dania.one.Model;

public class MsgServerModel {

    private int timestamp;
    private String time;
    private String date;
    private String msg;
    private String type;
    private String sender;
    private String msg_id;
    private String uri;
    private String extra;

    public MsgServerModel(int timestamp, String time, String date, String msg, String type, String sender, String msg_id, String uri, String extra) {
        this.timestamp = timestamp;
        this.time = time;
        this.date = date;
        this.msg = msg;
        this.type = type;
        this.sender = sender;
        this.msg_id = msg_id;
        this.uri = uri;
        this.extra = extra;
    }

    public MsgServerModel() {
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }
}
