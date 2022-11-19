package com.cse.accessigexcel;

import java.util.ArrayList;
import java.util.HashMap;

public class FinalData {
    String day,section_id;
    ArrayList<String> subjects;
    ArrayList<String> rooms;
    HashMap<String,ArrayList<String>> faculty_names;

    public FinalData() {
        this.day = null;
        this.section_id = null;
        this.subjects = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.faculty_names = new HashMap<String,ArrayList<String>>();
    }

    public void setDetails(String day,String section_id,ArrayList<String> subjects,ArrayList<String> rooms,HashMap<String,ArrayList<String>> faculty_names) {
        this.day = day;
        this.section_id = section_id;
        this.subjects = subjects;
        this.rooms = rooms;
        this.faculty_names = faculty_names;
    }

    public PeriodDetails[] getDetails() {
        PeriodDetails[] details= new PeriodDetails[subjects.size()+1];
        int i=0,periodno=1;
        ArrayList<String> faculties=null;
        for(i=0;i<this.subjects.size() && i<this.rooms.size();i++) {
            //System.out.println("faculty names for "+subjects.get(i)+" : "+faculty_names.get(subjects.get(i).trim()));

            faculties = faculty_names.get(subjects.get(i).trim());
            if(faculties == null) {
                String name = this.section_id+this.day+periodno+this.rooms.get(i);
                faculties = faculty_names.get(name);
            }
            //System.out.println(periodno+"-->"+section_id+"-->"+day+"-->"+subjects.get(i)+"-->"+rooms.get(i)+"-->"+faculties);
            details[periodno] = new PeriodDetails(periodno,this.section_id,this.day,this.subjects.get(i),this.rooms.get(i),faculties);
            periodno+=1;
        }
        return details;
    }

}
