package javaapplication9;

import java.util.*;
import java.time.LocalDate;

import java.io.*;

import java.time.LocalDate;

public class admin extends User implements Serializable{
    private static ArrayList<User> users= new ArrayList<>();
    //(String id,String email,String name,String password)
    public admin(String id,String email,String name,String password){
        super(id,email,name,password);
    }
    
    public static ArrayList<User> getallUsers() {
    return users;
}
    
    public void setallUsers(ArrayList<User>u1){users=new ArrayList<User>(u1);}
    
    public void adduser(User u1) throws Exception{
        if(u1 != null&& !users.contains(u1)){
            users.add(u1);
            System.out.println("User added : "+ u1.getname());
        
        return;
        }
         throw new Exception("cant add user invalid credintials");
        
        
    }
    public void edituser(User u1){
        for(int i = 0; i < users.size();i++){
            if(users.get(i).getid().equals(u1.getid())){
                users.set(i,u1);
                System.out.println("User updated : "+ u1.getname());
                return;
            }
        }
        System.out.println("User not found");
    }
    public User searchuser(String id){
    for(User u : users){
        if(u.getid().equals(id)){
            return u; // Found it, return the object
        }
    }
    return null; // Not found
}
    public void deleteuser(User u1){
        if(users.contains(u1)){
           users.remove(u1); System.out.println("User removed : "+ u1.getname());
        }
        else{
            System.out.println("User not found");
        }
    }
   
    public void view_rec_details(recruitment r1) {
    if (r1 == null) {
        System.out.println("Recruitment not found");
        return;
    }

    int count = 0;
    for (Application1 app : Application1.applications) {
        if (app.getRec_id().equals(r1.getRec_id())) {
            count++;
        }
    }

    System.out.println("Recruitment ID: " + r1.getRec_id());
    System.out.println("Job title: " + r1.getCategory());
    System.out.println("Recruitment Date: " + r1.getRecruitmentDate());
    System.out.println("Applications count: " + count);
}

    
    
    
 
    
//  public void max_recnum_ofjobtitle(Company c1) throws Exception {
//    if (c1 == null || c1.getRecruitments().isEmpty()) {
//        System.out.println("No recruitments found for this company.");
//        return;
//    }
//
//    String maxCategory = "";
//    int maxCount = 0;
//
//    // Loop through each recruitment in the company
//    for (recruitment r : c1.getRecruitments()) {
//        int count = 0;
//
//        // Count applications for this recruitment
//        for (Application app : Application.applications) {
//            if (app.getRec_id().equals(r.getRec_id())) {
//                count++;
//            }
//        }
//
//        // Update max if this recruitment has more applications
//        if (count > maxCount) {
//            maxCount = count;
//            maxCategory = r.getCategory();
//        }
//    }
//
//    if (maxCount > 0) {
//        System.out.println("Job category with max applications: " + maxCategory +
//                           " (" + maxCount + " applications)");
//    } else {
//        System.out.println("No applications found for this company.");
//    }
//}
//    
    
    
    // the period is estimated over month or more 
   public double getAverageRecruitments(Company c1,LocalDate start, LocalDate end) {
        int total = c1.getTotalRecruitments(start, end);
        int months = (end.getYear() - start.getYear()) * 12 + (end.getMonthValue() - start.getMonthValue() + 1);
        return months > 0 ? (double) total / months : 0.0;
    }

    
        //• Company with the maximum no. of Recruitments and their Categories. 

   public void top_applied_company(ArrayList<Company> companies) throws Exception {
    int max = 0;
    Company top = null;

    for (Company c : companies) {
        int count = 0;
        for (recruitment r : c.getRecruitments()) {
            for (Application1 app : Application1.applications) {
                if (app.getRec_id().equals(r.getRec_id())) {
                    count++;
                }
            }
        }
        if (count > max) {
            max = count;
            top = c;
        }
    }

    if (top != null) {
        System.out.println("Top applied company: " + top.getname() +
                           " (" + max + " applications)");
    } else {
        System.out.println("No applications found.");
    }
}
//• Category/Job Title with maximum no. of Recruitments over a specific period of time. 

   public String top_applied_category(Company c1, LocalDate start, LocalDate end) throws Exception {
    if (c1 == null || c1.getRecruitments().isEmpty()) {
        return "No recruitments found for this company.";
    }

    String topCategory = "";
    int maxCount = 0;

    // Loop through each recruitment in the company
    for (recruitment r : c1.getRecruitments()) {
        int count = 0;

        // Count applications for this recruitment within the date range
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
            topCategory = r.getCategory();
        }
    }

    return maxCount > 0 ? topCategory : "No applications found in this period.";
}


    public void no_of_recrutiment_percomp(Company c1)throws Exception{
        System.out.println("Number of recritments: "+c1.getRecruitments().size());
    }
    
    
    public void no_of_recnum_per_app(Applicant a1) {
    if (a1 == null) {
        System.out.println("Applicant not found.");
        return;
    }

    int count = 0;
    for (Application1 app : Application1.applications) {
        if (app.getApplicant_id().equals(a1.getid())) {
            count++;
        }
    }

    System.out.println("Number of applications submitted by applicant " 
                       + a1.getname() + ": " + count);
}
    
    
    
    
    
   
//     public String mostappliedjobtitle(Company c1)throws Exception{
//        if(c1 == null || c1.getRecruitments().isEmpty()){
//            return "";
//        }
//        int max = 0;
//        String jobtitle ="";
//        for(recruitment r : c1.getRecruitments()){
//            int count = r.getA1().size();
//            if(count > max){
//                max = count;
//                jobtitle = r.getCategory();
//            }
//        }
//        return jobtitle;
//    }
   public int max_recnum_by_applicant(ArrayList<Applicant> applicants) {
    if (applicants == null || applicants.isEmpty()) {
        System.out.println("No applicants found.");
        return 0;
    }

    int maxCount = 0;
    String topApplicantId = "";

    for (Applicant a : applicants) {
        int count = 0;

        // Count applications for this applicant
        for (Application1 app : Application1.applications) {
            if (app.getApplicant_id().equals(a.getid())) {
                count++;
            }
        }

        // Update max if this applicant has more applications
        if (count > maxCount) {
            maxCount = count;
            topApplicantId = a.getid();
        }
    }

    System.out.println("Applicant with max applications: " + topApplicantId +
                       " (" + maxCount + " applications)");
    return maxCount;
}
}
