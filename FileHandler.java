package javaapplication9;
import java.io.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */


public class Company extends User implements Serializable{
  // private String company_name;
    private  ArrayList<recruitment> recruitments = new ArrayList<>();

    public Company(String id,String email,String name,String password){
    super( id, email, name, password);
    }
    public ArrayList<recruitment> getRecruitments()  {
        return recruitments;
    }

    public void setRecruitments(ArrayList<recruitment> recruitments) {
        this.recruitments = new ArrayList<>(recruitments);
    }
    
    public void addRecruitment(recruitment r1) throws Exception{ if(recruitments.contains(r1))throw new Exception("recruitment already exist in company"); 
    else
    recruitments.add(r1);};
    
    
    // make exception 
   public void updateRecruitment(recruitment r1) {
    boolean found = false;
    for (int i = 0; i < recruitments.size(); i++) {
        // Check if IDs match
        if (recruitments.get(i).getRec_id().equals(r1.getRec_id())) { 
            
            // 1. Update the Job
            recruitments.set(i, r1);
            
            // 2. REMOVE LINKED APPLICATIONS (The "Easy" Way)
            // A. Create a list to hold items to delete
            ArrayList<Application1> toRemove = new ArrayList<>();

            // B. Find the applications
            for (Application1 app : Application1.applications) {
                if (app.getRec_id().equals(r1.getRec_id())) {
                    toRemove.add(app); // Add to trash bag
                }
            }

            // C. Delete them all at once
            Application1.applications.removeAll(toRemove);

            System.out.println("Recruitment updated and applications removed.");
            found = true;
            return;
        }
    }
    
    if (!found) {
        System.out.println("Recruitment does not exist (ID not found).");
    }
}
    
    
    
    
//    public void updateRecruitment(recruitment r1){
//        
//        for(int i=0;i<recruitments.size();i++){
//        if(recruitments.get(i).getCategory()==r1.getCategory()){
//        
//        recruitments.set(i,r1);
//        return;
//        
//        }
//        else{
//        
//            System.out.println("recruitment dont exist");
//        }
//        
//        }
//    }
    
//    public void updateRecruitment(recruitment r1) throws Exception {
//    boolean found = false;
//    
//    for(int i = 0; i < recruitments.size(); i++) {
//        if(recruitments.get(i).getCategory().equals(r1.getCategory())) { 
//            recruitments.set(i, r1);
//            found = true;
//            break; 
//        }
//    }
//
//    if (!found) {
//        throw new IllegalArgumentException("Recruitment does not exist, cannot update.");
//    }
//}
    
    
    
    

    public void deleteRecruitment(recruitment r1)throws Exception{
        
        
        if(recruitments.contains(r1)&&!recruitments.isEmpty())
        recruitments.remove(r1);
        else 
            throw new Exception("cant delete");
    };
    
    public void ListRecruitment (String rec_id){
        for (recruitment r : recruitments) {
            if (r.getRec_id().equals(rec_id)) {  
                System.out.println("Recruitment details: " + r.toString() );
                return;
            }
        }
        System.out.println("Recruitment with ID '" + rec_id + "' not found!");
    };   
    
    
    
    
    
    // In Company.java

// Import this at the top if missing: import java.time.LocalDateTime;

public void scheduleInterview(String rec_id, String app_id, LocalDateTime date) {
    boolean found = false;
    for (Application1 app : Application1.applications) {
        // Find application matching both Job ID and Applicant ID
        if (app.getRec_id().equals(rec_id) && app.getApplicant_id().equals(app_id)) {
            
            app.set_interview_timing(date);
            
            System.out.println("Interview scheduled for Applicant " + app_id + " on " + date);
            found = true;
            return;
        }
    }
    
    if (!found) {
        System.out.println("Error: Application not found for Interview scheduling.");
    }
}
    
    
    
    
    
    
//set condition to check the exitence of application in 
 public void acceptApplicant(String applicationId) {
    for (Application1 app : Application1.applications) {
        if (app.get_application_id().equals(applicationId)) {
            // Ensure the application belongs to one of this company's recruitments
            for (recruitment r : recruitments) {
                if (r.getRec_id().equals(app.getRec_id())) {
                    app.set_acceptance_status(true);
                    System.out.println("Applicant " + app.getApplicant_id() +
                                       " accepted for recruitment " + r.getCategory());
                    return;
                }
            }
        }
    }
    System.out.println("Application not found for this company.");
}

public void rejectApplicant(String applicationId) {
    for (Application1 app : Application1.applications) {
        if (app.get_application_id().equals(applicationId)) {
            for (recruitment r : recruitments) {
                if (r.getRec_id().equals(app.getRec_id())) {
                    app.set_acceptance_status(false);
                    System.out.println("Applicant " + app.getApplicant_id() +
                                       " rejected for recruitment " + r.getCategory());
                    return;
                }
            }
        }
    }
    System.out.println("Application not found for this company.");
}
    
   public String mostAppliedJob(LocalDate start, LocalDate end) {
    String mostAppliedCategory = "";
    int maxCount = 0;

    for (recruitment r : recruitments) {
        int count = 0;

        // Count applications for this recruitment
        for (Application1 app : Application1.applications) {
            if (app.getRec_id().equals(r.getRec_id())
                && !app.get_application_date().isBefore(start)
                && !app.get_application_date().isAfter(end)) {
                count++;
            }
        }

        // Update max if this recruitment has more applications
        if (count > maxCount) {
            maxCount = count;
            mostAppliedCategory = r.getCategory();
        }
    }

    return mostAppliedCategory;
}


    
    
public void viewApplicantsForRecruitment(String rec_id) {
    System.out.println("Applicants for recruitment ID: " + rec_id);
    
    boolean found = false; 

    for (Application1 app : Application1.applications) {
        if (app.getRec_id().equals(rec_id)) {
            System.out.println("Applicant ID: " + app.getApplicant_id());
            System.out.println(app.get_acceptance_status() ? "Accepted" : "Pending/Rejected");
            found = true; // <--- MARK AS FOUND
        }
    }

    // ONLY print error if 'found' is still false
    if (!found) {
        System.out.println("Applicant with ID " + rec_id + " not found.");
    }
}
    
     public int getTotalRecruitments(LocalDate start, LocalDate end) {
        int count = 0;
        for (recruitment r : recruitments) {
            if (!r.getRecruitmentDate().isBefore(start) && !r.getRecruitmentDate().isAfter(end)) {
                count++;
            }
        }
        return count;
    }

    
    
}