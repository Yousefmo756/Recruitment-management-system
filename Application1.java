 package javaapplication9;


import java.util.*;
import java.lang.Exception;
import java.io.Serializable;
import java.io.*;
public class Applicant extends User implements Serializable  {
    
    //(String id,String email,String name,String password)
   public Applicant (String id,String email,String name,String password){
    
        super( id, email,name, password);
    }
   
 private String myCV = ""; // Default empty CV

public String getCv() {
    return myCV;
}

public void setCv(String myCV) {
    this.myCV = myCV;
}
    
   void search_application(Application1 a) throws Exception {
    if (Application1.applications.contains(a) && a.getApplicant_id().equals(this.getid())) {
        System.out.println("Application id: " + a.get_application_id());
    } else {
        throw new Exception("Application does not exist for this applicant");
    }
}
    
public void applyforjob(Application1 application, recruitment r1) throws Exception {
    // Prevent duplicate applications by same applicant to same recruitment
    for (Application1 app : Application1.applications) {
        if (app.getApplicant_id().equals(this.getid()) && app.getRec_id().equals(r1.getRec_id())) {
            throw new Exception("Already applied to this recruitment");
        }
    }

    // Link applicant and recruitment
    application.setApplicant_id(this.getid());
    application.setRec_id(r1.getRec_id());

    // Add to global applications list
    Application1.applications.add(application);
    System.out.println("Application submitted: " + application.get_application_id() +
                       " by applicant " + this.getname() +
                       " for recruitment " + r1.getCategory());
}
    
    
public void view_application_updates() {
    for (Application1 a : Application1.applications) {
        if (a.getApplicant_id().equals(this.getid())) {
            String status = a.get_acceptance_status() ? "Accepted" : "Rejected/Pending";
            System.out.println("Application " + a.get_application_id() +
                               " for recruitment " + a.getRec_id() +
                               " is " + status);
        }
    }
}
    

   
}
