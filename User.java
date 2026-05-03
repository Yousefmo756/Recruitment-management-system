package javaapplication9;

    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    
    import java.util.Collections;
    import java.io.*;
    import java.time.LocalDate;
    import java.util.ArrayList;
    /**
     *
     * @author Lenovo
     */
    public class recruitment implements Serializable {
            private String rec_id , category,company_id ;
            //   year/month/Day
   private  LocalDate recruitmentDate;
    public recruitment(String rec_id, String category, String company_id, LocalDate recruitmentDate) {
    this.rec_id = rec_id;
    this.category = category;
    this.company_id = company_id;
    this.recruitmentDate = recruitmentDate;
}
    
    
    
    
public String getCategory() {
            return category;
        }
        public String getRec_id() {
            return rec_id;
        }
        

      

        public void setRec_id(String rec_id) {
            this.rec_id = rec_id;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public LocalDate getRecruitmentDate()  {
    return recruitmentDate;

    }
          public void setRecruitmentDate(LocalDate recruitmentDate )  {
    this.recruitmentDate=recruitmentDate;

    }
        //new mthod

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

          
          
          @Override
      public String toString() {
            return "Recruitment{" +
                    "rec_id='" + rec_id + '\'' +
                    ", category='" + category + '\'' +
                    ", recruitmentDate=" + recruitmentDate +
                    '}';
        }



    }