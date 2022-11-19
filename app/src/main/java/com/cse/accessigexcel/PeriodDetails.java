package com.cse.accessigexcel;

import java.util.ArrayList;

public class PeriodDetails {
    int periodno;
    String Section_name="",subject_name="",room_id="",day="";
    ArrayList<String> faculty_names;
    String faculty="";
    String time="";

    PeriodDetails() {
    }

    public PeriodDetails(int periodno, String Section_name,String day, String subject_name, String room_id, ArrayList<String> faculty_names) {
        this.periodno = periodno;
        this.Section_name = Section_name;
        this.subject_name = subject_name;
        this.room_id = room_id;
        this.day = day;
        this.faculty_names = faculty_names;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public int getPeriodno() {
        return periodno;
    }

    public void setPeriodno(int periodno) {
        this.periodno = periodno;
    }

    public String getSection_name() {
        return Section_name;
    }

    public void setSection_name(String Section_name) {
        this.Section_name = Section_name;
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

    public ArrayList<String> getFaculty_names() {
        return faculty_names;
    }

    public void setFaculty_names(ArrayList<String> faculty_names) {
        this.faculty_names = faculty_names;
    }


}
