package com.dania.one.Model;

import java.util.ArrayList;

public class MonthWiseStoriesModel {

    private String month;
    private ArrayList<DateWiseStoriesModel> dwsms;

    public MonthWiseStoriesModel(String month, ArrayList<DateWiseStoriesModel> dwsms) {
        this.month = month;
        this.dwsms = dwsms;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public ArrayList<DateWiseStoriesModel> getDwsms() {
        return dwsms;
    }

    public void setDwsms(ArrayList<DateWiseStoriesModel> dwsms) {
        this.dwsms = dwsms;
    }
}
