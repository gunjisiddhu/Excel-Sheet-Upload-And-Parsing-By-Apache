package com.cse.accessigexcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button load;
    String extension="";
    Spinner YearSelection;
    String year="",siddhu="";
    String[] timings = {"8:05-9:00","9:00-9:55","10:15-11:10","11:10-12:05","12:05-01:00","02:00-02:55","02:55-03:50","03:50-4:40","04:40-05:30"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermissionAndBrowseFile();

        YearSelection = findViewById(R.id.years);
        YearSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = YearSelection.getSelectedItem().toString();
                siddhu = YearSelection.getSelectedItem().toString();
                //MyApplication.FirebaseYear = year;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        load = findViewById(R.id.browse);
        load.setOnClickListener(view -> {

            String[] mimetypes =
                    { "application/vnd.ms-excel",
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    };
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            startActivityForResult(intent, 100);

        });

    }
    private void askPermissionAndBrowseFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()){

                // If you don't have access, launch a new activity to show the user the system's dialog
                // to allow access to the external storage
            }else{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri content_describer = data.getData();
        String type="";

        InputStream in = null;
        OutputStream out = null;
        try {
            // open the user-picked file for reading:
            try {
                in = getContentResolver().openInputStream(content_describer);
                type=getContentResolver().getType(content_describer);
                Log.d("path1",type);
                if(type.equalsIgnoreCase("application/vnd.ms-excel")){
                    extension=".xls";
                }
                else if(type.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                {
                    extension=".xlsx";
                }
                else{
                    extension=".xls";
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // open the output-file:
            try {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TimeTable");
                if (!file.exists()) {
                    file.mkdirs();
                }
                out = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString()+"/TimeTable/data"+extension));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // copy the content:
            byte[] buffer = new byte[1024];
            int len=0;
            while (true) {
                try {
                    if (!((len = in.read(buffer)) != -1)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.write(buffer, 0, len);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Contents are copied!
            try {
                ExtractData(Environment.getExternalStorageDirectory()+"/TimeTable/data"+extension);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ExtractData(String fileLocation) throws IOException {

        ArrayList<SectionModal> arr=new ArrayList<>();
        HashSet<String> days = new HashSet<>();
        days.add("Day");
        days.add("Mon");
        days.add("Tue");
        days.add("Wed");
        days.add("Thu");
        days.add("Fri");
        days.add("Sat");
        FileInputStream file = new FileInputStream(new File(fileLocation));
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        System.out.println(year+"  selected");
        XSSFSheet sheet = workbook.getSheet(year);
        //String name = "IV - CSE";
        //XSSFSheet sheet = workbook.getSheet("III - CSE");
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {

            CellRangeAddress c = sheet.getMergedRegion(i);
            String val=sheet.getRow(c.getFirstRow()).getCell(c.getFirstColumn()).getStringCellValue();
            int firstCol=c.getFirstColumn();
            int lastCol=c.getLastColumn();
            int startRow=c.getFirstRow();
            int lastRow=c.getLastRow();

            for(int x=startRow;x<=lastRow;x+=1)
                for(int y=firstCol;y<=lastCol;y+=1)

                    sheet.getRow(x).getCell(y).setCellValue(val);

        }

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();
            if(cellIterator.hasNext())
            {
                Cell c=cellIterator.next();
                SectionModal curSec=new SectionModal();
                //checking wheather this is start of a section.

                if(c.getRichStringCellValue().getString().contains("Section")) {
                    String section = c.getRichStringCellValue().getString();
                    System.out.println(section);
                    int colonIndex=section.indexOf(":");
                    int pranthesisIndex = section.indexOf("(");
                    if(pranthesisIndex==-1) {
                        pranthesisIndex = section.length();
                    }
                    curSec.SectionName=section.substring(colonIndex+1,pranthesisIndex).trim();

                    if(pranthesisIndex != section.length()) {
                        curSec.DefaultRoom = section.substring(pranthesisIndex+1, section.length()-1).trim();
                    }else {
                        curSec.DefaultRoom = "-";
                    }
                }
                else {
                    continue;
                }

                // moving to the timing rows.
                row = rowIterator.next();
                ArrayList<String>timings=new ArrayList<>();
                cellIterator = row.cellIterator();

                // reterviving the timings of sections.
                while(cellIterator.hasNext()) {
                    timings.add(cellIterator.next().getRichStringCellValue().getString());
                }
                curSec.timings=timings;
                row = rowIterator.next();
                //reterviving days of sections.
                //movig to sections.
                String prev="";
                while(days.contains(row.getCell(0).getRichStringCellValue().getString())) {
                    ArrayList<String>dayPeriods=new ArrayList<>();

                    String curDay=row.getCell(0).getRichStringCellValue().getString();
                    cellIterator=row.cellIterator();
                    while(cellIterator.hasNext())
                    {
                        String cur=cellIterator.next().getRichStringCellValue().getString();
                        if(cur.indexOf("\n")!=-1) {
                            cur = cur.replace("\n","");
                        }
                        prev=cur;
                        dayPeriods.add(cur);
                    }
                    curSec.setDay(curDay, dayPeriods);

                    if(rowIterator.hasNext())
                        row=rowIterator.next();
                    else
                        break;
                }
                arr.add(curSec);

                HashMap<String,ArrayList<String>> facultyDealings = new HashMap<>();
                curSec.faculty = facultyDealings;
                while( row.getFirstCellNum()!=-1 && row.iterator().hasNext()&& (row.getCell(0)!=null) &&!row.getCell(0).getStringCellValue().trim().equals("")) {

                    cellIterator = row.iterator();
                    while(cellIterator.hasNext()) {
                        String value = cellIterator.next().getStringCellValue();
                        if(value.indexOf(":")==-1) {
                            continue;
                        }
                        String key=value.substring(0,value.indexOf(":")).trim();
                        value=value.substring(value.indexOf(":")+1).trim();
                        ArrayList<String> names = new ArrayList<>();
                        String[] facultynames = value.split(",");
                        for(String facultyname : facultynames) {
                            facultyname = facultyname.replaceAll("[-+.^:, ]", "").toLowerCase(Locale.ROOT);
                            names.add(facultyname);
                        }

                        facultyDealings.put(key,names);
                        //faclty name tho hashmap create cheyaliii

                    }
                    if(!rowIterator.hasNext()) {
                        break;
                    }else {
                        row = rowIterator.next();
                    }
                }
                curSec.faculty = facultyDealings;
            }else {
                continue;
            }
        }

        file.close();

        FinalData details= new FinalData();
        PeriodDetails[] data;

        if(checkYearNotContainsCSE()) {
            year = year.substring(0,year.length())+" - ";
        }else {
            year = year.substring(0,year.lastIndexOf(" ")+1);
        }
        int index=0;
        for(SectionModal x: arr) {
            for(String day : days) {
                details.setDetails(day,x.SectionName,x.getParticularDay(day),x.getRooms(day),x.faculty);
                data = details.getDetails();
                for( int i=1;i<data.length;i++) {

                    //System.out.println(year+data[i].getSection_name()+"-->"+data[i].getPeriodno()+"-->"+data[i].getDay()+"-->"+data[i].getSubject_name()+"-->"+data[i].getRoom_id()+"-->"+data[i].getFaculty_names());
                    data[i].setSection_name(year + data[i].getSection_name());
                    data[i].setTime(timings[data[i].getPeriodno()-1]);

                    //student saving
                    /*if(data[i].getPeriodno()<8) {

                        if (data[i].getFaculty_names() != null) {
                            String name = data[i].getFaculty_names().get(0);
                            data[i].setFaculty(name);
                            StudentsDataSaving(data[i]);
                        } else {
                            data[i].setFaculty("-");
                            StudentsDataSaving(data[i]);
                        }
                    }*/

                    //faculty saving
                    if(data[i].getPeriodno()<8) {
                        if (data[i].getFaculty_names() != null) {
                            index = 0;
                            while (index < data[i].getFaculty_names().size()) {
                                String name = data[i].getFaculty_names().get(index);
                                data[i].setFaculty(name);
                                PeriodDetails period = new PeriodDetails(data[i].getPeriodno(),data[i].getSection_name(),data[i].getDay(),data[i].getSubject_name(),data[i].getRoom_id(),data[i].getFaculty_names());
                                period.setFaculty(name);
                                period.setTime(data[i].getTime());

                                if(index > 0) {
                                    String facultyname = data[i].getFaculty_names().get(index-1);
                                    data[i].setFaculty(facultyname);
                                    prevperiodDetails = new PeriodDetails(data[i].getPeriodno(),data[i].getSection_name(),data[i].getDay(),data[i].getSubject_name(),data[i].getRoom_id(),data[i].getFaculty_names());
                                    prevperiodDetails.setFaculty(facultyname);
                                    prevperiodDetails.setTime(data[i].getTime());
                                    //FacultiesDataSaving(period);
                                }
                                FacultiesDataSaving(period);
                                index += 1;
                            }
                        } else {
                            data[i].setFaculty("-");
                            FacultiesDataSaving(data[i]);
                            prevperiodDetails = null;

                        }
                    }
                }
            }
            System.out.println();
        }

    }

    private boolean checkYearNotContainsCSE() {
        if (!year.contains("CSE"))
            return  true;
        else
            return false;
    }

    public PeriodDetails prevperiodDetails=null;
    private void FacultiesDataSaving(PeriodDetails details) {

        /*
        *
        * method name FacultiesDataSaving, it saves
        * for both faculty and students.
        * Students appeared for here for few cases.
        * see below documentation and implementation.
        *
        * */

        System.out.println(details.getSection_name()+"-->"+details.getPeriodno()+"-->"+details.getDay()+"-->"+details.getSubject_name()+"-->"+details.getRoom_id()+"-->"+details.getFaculty());
        DatabaseReference databaseReferenceforFaculty = FirebaseDatabase.getInstance().getReference("TimeTableData").child("FacultyDetails");
        DatabaseReference databaseReferenceforStudent = FirebaseDatabase.getInstance().getReference("TimeTableData").child("StudentDetails");


        /*
        * Here below if condition says that
        * subject name should not be like mentioned in if condition.
        *
        * */

        if(!details.getSubject_name().contains("***") && !details.getSubject_name().equals("Sat") ) {

            databaseReferenceforStudent.child(details.getSection_name()).child(details.getDay()).child(details.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Student studentFromFirebase = snapshot.getValue(Student.class);

                    if (!details.getFaculty().equalsIgnoreCase("None")) {
                        if (studentFromFirebase != null && !details.getSubject_name().contains("Sat") && !details.getSubject_name().equals("-")) {

                            if (!studentFromFirebase.getSub().equals(details.getSubject_name())) {

                                /*
                                 * When old period data and new period data
                                 * doesn't match.
                                 *
                                 * */

                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("TimeTableData").child("FacultyDetails");
                                String name = studentFromFirebase.getFaculty();
                                name = name.replaceAll("[-+.^:, ]", "").toLowerCase(Locale.ROOT);

                                Log.e("old data:" + studentFromFirebase.getSec() + ", " + studentFromFirebase.getPer() + ", " + studentFromFirebase.getTime() + ", " + studentFromFirebase.getDay() + ", " + studentFromFirebase.getSub() + ", ", studentFromFirebase.getRoom() + ", " + studentFromFirebase.getFaculty());
                                Log.e("new data:" + details.getSection_name() + ", " + details.getPeriodno() + ", " + details.getTime() + ", " + details.getDay() + ", " + details.getSubject_name() + ", ", details.getRoom_id() + ", " + details.getFaculty());


                                /* Reterviving old Faculty by there name from Firebase to
                                 * delete it and upload new data for student.
                                 * the faculty saving will done in save() method below.
                                 */

                                databaseReference1.child(name).child(studentFromFirebase.getDay()).child(siddhu).child(studentFromFirebase.getTime()).removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        DatabaseReference studentsaving = FirebaseDatabase.getInstance().getReference("TimeTableData").child("StudentDetails");
                                        Student newstudent = new Student(details.getPeriodno(), details.getTime(), details.getSection_name(), details.getSubject_name(), details.getRoom_id(), details.getDay(), details.getFaculty());
                                        studentsaving.child(details.getSection_name()).child(details.getDay()).child(details.getTime()).setValue(newstudent).addOnCompleteListener(task14 -> {
                                            if (task14.isSuccessful()) {
                                                Log.e("the above changes updated", "");
                                            }
                                        });
                                    }
                                });
                            }


                            /* when period same once data reupload to firebase.
                             * The above mentioned that faculty data will save in save() method,
                             * it upload upload new data for faculty.
                             *
                             */

                            save();
                        }
                    } else {

                        /* When a faculty is not present
                         *
                         * */

                        if (studentFromFirebase != null) {

                            /*A period where non Teaching Subject like library,
                             * and old faculty period details will be deleted,
                             * because period has changed.
                             *
                             */

                            String name = studentFromFirebase.getFaculty();
                            name = name.replaceAll("[-+.^:, ]", "").toLowerCase(Locale.ROOT);
                            FirebaseDatabase.getInstance().getReference("TimeTableData").child("FacultyDetails").child(name).child(studentFromFirebase.getDay()).child(siddhu).child(studentFromFirebase.getTime()).removeValue().addOnCompleteListener(task -> {
                                Student newstudent = new Student(details.getPeriodno(), details.getTime(), details.getSection_name(), details.getSubject_name(), details.getRoom_id(), details.getDay(), details.getFaculty());
                                FirebaseDatabase.getInstance().getReference("TimeTableData").child("StudentDetails").child(siddhu).child(details.getSection_name()).child(details.getDay()).child(details.getTime()).setValue(newstudent).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.e("data updated for periods", "when no faculty comes");
                                    }
                                });
                            });
                        } else {

                            /*A period it is not existing,
                             * need to save for both Faculty and Students
                             *
                             * */

                            DatabaseReference facultySaving = FirebaseDatabase.getInstance().getReference("TimeTableData").child("FacultyDetails");
                            DatabaseReference studentsaving1 = FirebaseDatabase.getInstance().getReference("TimeTableData").child("StudentDetails");

                            if (studentFromFirebase == null && details.getFaculty() != null) {

                                /*A period  it is not existing,
                                 * need to save for both Faculty and Students,
                                 * because period has changed.
                                 *
                                 */

                                Student students = new Student(details.getPeriodno(), details.getTime(), details.getSection_name(), details.getSubject_name(), details.getRoom_id(), details.getDay(), details.getFaculty());
                                Faculty facultyData = new Faculty(details.getPeriodno(), details.getSection_name(), details.getSubject_name(), details.getRoom_id(), details.getDay(), details.getFaculty(), details.getTime());
                                String result = details.getFaculty().replaceAll("[-+.^:, ]", "").toLowerCase(Locale.ROOT);
                                facultySaving.child(result).child(details.getDay()).child(siddhu).child(details.getTime()).setValue(facultyData).addOnCompleteListener(task -> {
                                    studentsaving1.child(details.getSection_name()).child(details.getDay()).child(details.getTime()).setValue(students).addOnCompleteListener(task12 -> Log.e("if period doesn't", " exist in database will save now"));
                                });
                            } else {

                                /*A period it is not existing,
                                 * Faculty is also not existing need to save for only students,
                                 * because period has changed.
                                 *
                                 */


                                Student studentDataWhenNoFaculty = new Student(details.getPeriodno(), details.getTime(), details.getSection_name(), details.getSubject_name(), details.getRoom_id(), details.getDay(), details.getFaculty());
                                studentsaving1.child(details.getSection_name()).child(details.getDay()).child(details.getTime()).setValue(studentDataWhenNoFaculty).addOnCompleteListener(task -> {
                                    Log.e("no faculty for ", "this periods");
                                });
                            }
                        }
                    }
                }
                private void save() {


                    /*
                    * save method will saves for faculty data.
                    * if faculty doesn't exist it will save only for a student.
                    * if faculty exist it will save for both Faculty and Student.
                    * here lab and non lab periods are differentiated see below implementation.
                    *
                     */

                    DatabaseReference studentsaving = FirebaseDatabase.getInstance().getReference("TimeTableData").child("StudentDetails");
                    Faculty facultyData = new Faculty(details.getPeriodno(),details.getSection_name(),details.getSubject_name(),details.getRoom_id(),details.getDay(),details.getFaculty(),details.getTime());
                    String name = details.getFaculty();
                    String result = name.replaceAll("[-+.^:,]", "");

                    databaseReferenceforFaculty.child(result).child(details.getDay()).child(siddhu).child(details.getTime()).setValue(facultyData).addOnCompleteListener(task13 ->{
                        if(task13.isSuccessful()) {
                            if(details.getFaculty()!="-") {

                                /*
                                * when faculty is present
                                *
                                 */

                                if(details.getFaculty_names().size()>=2) {

                                    /*
                                    * When Faculties are more than 1 need to set for student main faculty
                                    * that is first faculty name present in details.getFaculty_name().get(0).
                                    * it can said as subject with more than one faculty.
                                    * see below implementation.
                                    *
                                     */

                                    Student student = new Student(details.getPeriodno(),details.getTime(), details.getSection_name(),details.getSubject_name(), details.getRoom_id(),details.getDay(), details.getFaculty_names().get(0));
                                    studentsaving.child(details.getSection_name()).child(details.getDay()).child(details.getTime()).setValue(student).addOnCompleteListener(task -> {
                                        Log.e("Lab data saved","saved");
                                    });
                                }else {

                                    /*
                                     * When Faculty is exactly 1, save for student that faculty
                                     * as there faculty for mentioned subject.
                                     * it can said as subject where not more than 1 faculty.
                                     * see below implementation.
                                     *
                                     */

                                    Student student = new Student(details.getPeriodno(),details.getTime(), details.getSection_name(),details.getSubject_name(), details.getRoom_id(),details.getDay(), details.getFaculty());
                                    studentsaving.child(details.getSection_name()).child(details.getDay()).child(details.getTime()).setValue(student).addOnCompleteListener(task -> {
                                        Log.e("Non Lab data saved","saved");
                                    });
                                }
                            }
                        }
                    });
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    private void StudentsDataSaving(@NonNull PeriodDetails details) {
        System.out.println(details.getSection_name()+" ==> "+details.getPeriodno()+" ==> "+details.getTime()+" ==> "+details.getDay()+" ==> "+details.getSubject_name()+" ==> "+details.getRoom_id()+" ==> "+details.getFaculty());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TimeTableData").child("StudentDetails");
        Student student = new Student(details.getPeriodno(),details.getTime(),details.getSection_name(),details.getSubject_name(),details.getRoom_id(),details.getDay(),details.getFaculty());
        /*databaseReference.child(details.getSection_name()).child(details.getDay()).child(details.getTime()).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.e("data saved","for students");
                }
            }
        });*/

    }
}