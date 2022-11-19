package com.cse.accessigexcel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

public class SectionModal {
    //String[] rooms = {"VPSF-01","VPSF-02","VPSF-03","VPSF-04","VPSF-05","VPSF-06","VPSF-07","VPSF-08","VPSF-09","VPSF-10","VPSF-11","VPSF-12"};
    String[] nonTeachingSubjects = {"Weekly Test","Open Elective","Library","Honors","Honors/SCIRP","Honors/Library","Open Elective/Library"};
    HashMap<String, ArrayList<String>> faculty;
    String SectionName;
    String DefaultRoom;
    HashMap<String,ArrayList<String>> days;
    HashMap<String,ArrayList<String>> dayRooms=new HashMap<>();
    ArrayList<String>timings;


    public SectionModal() {
        this.faculty = new LinkedHashMap<>();
        this.SectionName = null;
        this.days = new LinkedHashMap<>();

    }

    public ArrayList<String> getParticularDay(String day) {

        ArrayList<String> newperiod = new ArrayList<>();
        int i=1;
        int period=1;
        while(days.get(day)!=null && i< Objects.requireNonNull(days.get(day)).size()) {

            if(!Objects.requireNonNull(days.get(day)).get(i).trim().equals("Break")) {
                newperiod.add(Objects.requireNonNull(days.get(day)).get(i).equals("")?"-":periodFiltering(day,period, Objects.requireNonNull(days.get(day)).get(i)));
                period+=1;
            }
            i+=1;
        }
        return newperiod;
    }

    ArrayList<String> getFaculty(String subject) {
        return faculty.get(subject);
    }

    void setDay(String day,ArrayList<String> dayTimeTable) {
        ArrayList<String> newperiod = new ArrayList<>();
        int i=1;
        while(i<dayTimeTable.size()) {
            if(!dayTimeTable.get(i).trim().equals("Break")) {
                newperiod.add(dayTimeTable.get(i).equals("")?"-":dayTimeTable.get(i));
            }
            i+=1;
        }
        days.put(day,dayTimeTable);
        ArrayList<String> rooms = new ArrayList<>();

        for(String value : dayTimeTable) {
            String room;

            if(value.indexOf("[")!=-1) {

                room=value.substring(value.indexOf("[")+1,value.lastIndexOf("]"));

            }
            else {

                room = this.DefaultRoom;//.substring(1, this.DefaultRoom.length()-1);
            }
            rooms.add(room);
        }
        dayRooms.put(day, rooms);
    }

    public String roomFiltering(String room) {

        int flag=-1;
        if(room.indexOf("[")!=-1) {
            room = room.substring(room.indexOf("[")+1, room.lastIndexOf("]"));
        }else {
            for(String value : nonTeachingSubjects) {
                if(room.trim().equals(value) || room.equals("")) {
                    room = "-";
                    flag= 1;
                    break;
                }
            }
            if(flag == -1) {
                room = this.DefaultRoom;//.substring(1, this.DefaultRoom.length()-1);
            }

        }
        return room;
    }

    public String periodFiltering(String day,int periodno,String period) {
        if((period.indexOf("]")!=-1)&&period.lastIndexOf("]") != period.length()-1) {

            ArrayList<String> facultynames = new ArrayList<>();
            String name = period.substring(period.lastIndexOf("]")+1,period.length());
            name = name.replaceAll("[-+.^:, ]", "").toLowerCase(Locale.ROOT);
            facultynames.add(name);
            // key name is section+periodno+roomid for idp/scrip or even if cell contains names of faculties
            String subject_name = this.SectionName+day+periodno+period.substring(period.indexOf("[")+1,period.lastIndexOf("]"));
            this.faculty.put(subject_name,facultynames);
            period = period.substring(0,period.indexOf("["));
        }
        if(period.indexOf("[")!=-1){
            period = period.substring(0, period.indexOf("["));
            //System.out.println(period+"--> else");
        }


        return period;
    }

    @NonNull
    public String toString(){
        StringBuilder ans= new StringBuilder();
        for(String x:days.keySet())
            ans.append(days.get(x)).append("\n");
        return ans.toString();
    }

    public String getRoom(String day,int period) {
        ArrayList<String> curRooms = dayRooms.get(day);
        int i=1;
        int j=0;
        while(i<curRooms.size()) {

            if(!days.get(day).get(i).trim().equals("Break")) {
                j+=1;
            }
            if(period == j) {
                return curRooms.get(i);

            }

            i+=1;
        }
        return null;
    }

    public String getPeriod(ArrayList<String> periods,int x) {

        int y=0;
        for(int i=0;i<periods.size();i++) {

            if(!periods.get(i).equals("Break")) {
                y+=1;
            }
            if(x==y) {
                return periods.get(i);
            }
        }


        return null;
    }

    public ArrayList<String> getRooms(String day) {
        ArrayList<String> curRooms = dayRooms.get(day);
        ArrayList<String> newRooms = new ArrayList<>();
        int i=1;

        while(curRooms!=null && i<curRooms.size()) {
            //need to create new class or method to add periods properly.
            if(!days.get(day).get(i).trim().equals("Break")) {
                newRooms.add(roomFiltering(days.get(day).get(i)));
                //newRooms.add(days.get(day).get(i).equals("")?"-":curRooms.get(i));
            }
            i+=1;
        }
        return newRooms;
    }

}
