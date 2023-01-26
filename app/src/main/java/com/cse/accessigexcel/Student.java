package com.cse.accessigexcel;


public class Student {
    int per;
    String sec="",sub="",room="",day="";
    String faculty="";
    String time="";

    public Student() {

    }
    public Student(int per, String time,String sec, String sub, String room, String day, String faculty) {
        this.per = per;
        this.time = time;
        this.sec = sec;
        this.sub = sub;
        this.room = room;
        this.day = day;
        this.faculty = faculty;
    }

    public int getPer() {
        return per;
    }

    public void setPer(int per) {
        this.per = per;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
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

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
