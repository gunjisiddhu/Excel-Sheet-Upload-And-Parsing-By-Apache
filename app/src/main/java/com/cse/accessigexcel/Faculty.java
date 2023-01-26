package com.cse.accessigexcel;

public class Faculty {
    int period;
    String sectionId="",shortVal="",room="",day="";
    String name="";
    String time="";

//    private String day,name,sectionId,shortVal,room,time;
//    private long period;
    public Faculty(int period, String sectionId, String shortVal, String room, String day, String name, String time) {
        this.period = period;
        this.sectionId = sectionId;
        this.shortVal = shortVal;
        this.room = room;
        this.day = day;
        this.name = name;
        this.time = time;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getShortVal() {
        return shortVal;
    }

    public void setShortVal(String shortVal) {
        this.shortVal = shortVal;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
