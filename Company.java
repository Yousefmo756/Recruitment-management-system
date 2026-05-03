package javaapplication9;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.time.LocalDate;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.*;
/**
 *
 * @author Lenovo
 */
/*might change acceptancestatus as enum Acceptance_Status{
accepted;
rejected;

}*/
public class Application1 implements Serializable{
 public static ArrayList <Application1>applications= new ArrayList<>();
 private String applicant_id;
 private String rec_id;
   private  String application_id;
   private  String cv;
   //y/m/d
private LocalDate application_date;
//y/m/d/h
private LocalDateTime interview_timing;

boolean Acceptance_Status;
public Application1( String applicant_id,
 String rec_id,String application_id,String cv,LocalDate application_date,LocalDateTime interview_date,Boolean Acceptance_Status){
this.applicant_id=applicant_id;
this.rec_id=rec_id;
    this.application_id=application_id;
this.Acceptance_Status=Acceptance_Status;
this.application_date=application_date;
this.cv=cv;
this.interview_timing=interview_timing;
}

public String get_application_id(){return application_id;}
public void set_application_id(String application_id){this.application_id=application_id;}
public boolean get_acceptance_status(){return Acceptance_Status;}
public void set_acceptance_status(boolean Acceptance_Status){this.Acceptance_Status=Acceptance_Status;}
public LocalDateTime get_interview_timing() { return interview_timing; }
public void set_interview_timing(LocalDateTime interview_timing) { this.interview_timing = interview_timing; }
public void set_application_date(LocalDate application_date){  this.application_date=application_date;    };
public LocalDate get_application_date(){return application_date;}
public String get_cv(){return cv;}
public void set_cv(String cv){this.cv=cv;}

    public String getApplicant_id() {
        return applicant_id;
    }

    public void setApplicant_id(String applicant_id) {
        this.applicant_id = applicant_id;
    }


   


     public String getRec_id() {
            return rec_id;
        }
        

      

        public void setRec_id(String rec_id) {
            this.rec_id = rec_id;
        }

}
