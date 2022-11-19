package com.cse.accessigexcel;


public class Student {
    int periodno;
    String Section_name="",subject_name="",room_id="",day="";
    String faculty="";
    String time="";

    public Student() {

    }
    public Student(int periodno, String time,String section_name, String subject_name, String room_id, String day, String faculty) {
        this.periodno = periodno;
        this.time = time;
        Section_name = section_name;
        this.subject_name = subject_name;
        this.room_id = room_id;
        this.day = day;
        this.faculty = faculty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public int getPeriodno() {
        return periodno;
    }

    public void setPeriodno(int periodno) {
        this.periodno = periodno;
    }

    public String getSection_name() {
        return Section_name;
    }

    public void setSection_name(String section_name) {
        Section_name = section_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
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
}
