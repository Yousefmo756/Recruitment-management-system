package javaapplication9;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaApplication9 extends Application {

    private Stage window;
    private User currentUser;

    
   @Override
public void start(Stage primaryStage) {
    window = primaryStage;
    window.setTitle("Job Recruitment System");
    
    // ADD THIS LINE HERE:
    FileHandler.loadData(); 
    
    showLoginScene();
    window.show();
}
  
// This method runs automatically when you close the window
@Override
public void stop() {
    FileHandler.saveData(); 
    System.out.println("Data saved successfully to system_data.dat");
}





    // =====================================================================
    // 1. LOGIN SCENE
    // =====================================================================
    private void showLoginScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("SYSTEM LOGIN");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField email = new TextField(); email.setPromptText("Email");
        PasswordField pass = new PasswordField(); pass.setPromptText("Password");
        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register New Account");

        loginBtn.setOnAction(e -> {
            boolean found = false;
            // Loop through the static list in admin
            for (User u : admin.getallUsers()) {
                if (u.login(email.getText(), pass.getText())) {
                    currentUser = u;
                    found = true;
                    if (u instanceof admin) showAdminDashboard();
                    else if (u instanceof Company) showCompanyDashboard();
                    else if (u instanceof Applicant) showApplicantDashboard();
                    break;
                }
            }
            if (!found) new Alert(Alert.AlertType.ERROR, "Invalid Credentials").show();
        });

        registerBtn.setOnAction(e -> showUserForm(false, null)); 
        layout.getChildren().addAll(title, email, pass, loginBtn, registerBtn);
        window.setScene(new Scene(layout, 350, 450));
    }

    // =====================================================================
    // 2. USER FORM (Used for Add and Edit)
    // =====================================================================
    private void showUserForm(boolean isAdminRequest, User userToEdit) {
        VBox layout = new VBox(10); layout.setPadding(new Insets(20)); layout.setAlignment(Pos.CENTER);
        
        Label header = new Label(userToEdit == null ? "Add / Register User" : "Edit User");
        TextField id = new TextField(); id.setPromptText("Enter ID");
        TextField name = new TextField(); name.setPromptText("Full Name");
        TextField email = new TextField(); email.setPromptText("Email");
        PasswordField pass = new PasswordField(); pass.setPromptText("Password");
        ComboBox<String> roleChoice = new ComboBox<>();
        roleChoice.getItems().addAll("Admin", "Company", "Applicant");
        roleChoice.setPromptText("Role");

        // Pre-fill if Editing
        if (userToEdit != null) {
            id.setText(userToEdit.getid()); id.setDisable(true);
            name.setText(userToEdit.getname()); email.setText(userToEdit.getemail()); pass.setText(userToEdit.getpassword());
            roleChoice.setValue(userToEdit.getClass().getSimpleName().equals("admin") ? "Admin" : userToEdit.getClass().getSimpleName());
            roleChoice.setDisable(true);
        } else if(!isAdminRequest) roleChoice.setValue("Applicant");

        Button submitBtn = new Button("Submit");
        Button backBtn = new Button("Back");

        submitBtn.setOnAction(e -> {
            try {
                if (email.getText().isEmpty()) throw new Exception("Fill all fields");

                if (userToEdit == null) {
                    // --- ADD NEW USER ---
                    for(User u : admin.getallUsers()) if(u.getid().equals(id.getText())) throw new Exception("ID Taken");
                    
                    User newUser; String role = roleChoice.getValue();
                    if ("Admin".equals(role)) newUser = new admin(id.getText(), email.getText(), name.getText(), pass.getText());
                    else if ("Company".equals(role)) newUser = new Company(id.getText(), email.getText(), name.getText(), pass.getText());
                    else newUser = new Applicant(id.getText(), email.getText(), name.getText(), pass.getText());

                    // Use Admin Method if Admin is logged in
                    if (currentUser instanceof admin) ((admin) currentUser).adduser(newUser); 
                    else admin.getallUsers().add(newUser); // Public registration
                    
                    new Alert(Alert.AlertType.INFORMATION, "User Added").show();
                } else {
                    // --- EDIT EXISTING USER ---
                    userToEdit.setname(name.getText()); userToEdit.setemail(email.getText()); userToEdit.setpassword(pass.getText());
                    // Use Admin Method
                    if (currentUser instanceof admin) ((admin) currentUser).edituser(userToEdit);
                    new Alert(Alert.AlertType.INFORMATION, "User Updated").show();
                }
                if (isAdminRequest) showAdminDashboard(); else showLoginScene(); 
            } catch (Exception ex) { new Alert(Alert.AlertType.ERROR, ex.getMessage()).show(); }
        });

        backBtn.setOnAction(e -> { if (isAdminRequest) showAdminDashboard(); else showLoginScene(); });
        layout.getChildren().addAll(header, id, name, email, pass, roleChoice, submitBtn, backBtn);
        window.setScene(new Scene(layout, 350, 500));
    }

    // =====================================================================
    // 3. ADMIN DASHBOARD (Requirements Implemented Here)
    // =====================================================================
    private void showAdminDashboard() {
        admin adm = (admin) currentUser;
        VBox layout = new VBox(10); layout.setPadding(new Insets(20));
        Label title = new Label("ADMIN: " + adm.getname()); title.setStyle("-fx-font-weight: bold;");

        // --- REQUIREMENT: SEARCH (By Any Field) ---
        HBox searchBox = new HBox(10);
        TextField searchField = new TextField(); searchField.setPromptText("Search ID, Name, or Email...");
        Button btnSearch = new Button("Search");
        ListView<User> userList = new ListView<>();
        userList.getItems().addAll(admin.getallUsers());
        
        btnSearch.setOnAction(e -> {
            String term = searchField.getText().toLowerCase();
            userList.getItems().clear();
            boolean found = false;
            // Logic handled here since adm.searchuser() is missing
            for(User u : admin.getallUsers()) {
                if(u.getid().toLowerCase().contains(term) || 
                   u.getname().toLowerCase().contains(term) || 
                   u.getemail().toLowerCase().contains(term)) {
                    userList.getItems().add(u);
                    found = true;
                }
            }
            if(!found) {
                new Alert(Alert.AlertType.INFORMATION, "No users found").show();
                userList.getItems().addAll(admin.getallUsers());
            }
        });
        searchBox.getChildren().addAll(searchField, btnSearch);

        // --- REQUIREMENT: ADD / EDIT / REMOVE ---
        HBox actionBox = new HBox(10);
        Button btnAdd = new Button("Add");
        Button btnEdit = new Button("Edit");
        Button btnDelete = new Button("Remove");
        
        btnAdd.setOnAction(e -> showUserForm(true, null)); // Calls adduser logic
        btnEdit.setOnAction(e -> { User s = userList.getSelectionModel().getSelectedItem(); if(s!=null) showUserForm(true, s); }); // Calls edituser logic
        
        // Logic handled here since adm.deleteuser() is missing
        btnDelete.setOnAction(e -> { 
            User s = userList.getSelectionModel().getSelectedItem(); 
            if(s!=null && !s.equals(currentUser)){ 
                admin.getallUsers().remove(s); 
                userList.getItems().remove(s);
                System.out.println("User Removed");
            }
        });
        actionBox.getChildren().addAll(btnAdd, btnEdit, btnDelete);

        // --- REQUIREMENT: REPORTS ---
        Label rptLabel = new Label("Reports:"); rptLabel.setStyle("-fx-font-weight: bold;");
        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);

        // 1. Recruitments per Company
        Button r1 = new Button("1. Jobs per Company");
        r1.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("JOBS PER COMPANY:\n----------------\n");
            for(User u : admin.getallUsers()) {
                if(u instanceof Company) {
                    try { sb.append(u.getname()).append(": ").append(((Company)u).getRecruitments().size()).append(" recruitments\n"); } catch(Exception ex){}
                }
            }
            showReport("Recruitments Count", sb.toString());
        });

        // 2. Company with Max Recruitments
        Button r2 = new Button("2. Max Jobs Company");
        r2.setOnAction(e -> {
            Company maxC = null; int max = -1;
            for(User u : admin.getallUsers()) {
                if(u instanceof Company) {
                    try { if(((Company)u).getRecruitments().size() > max) { max = ((Company)u).getRecruitments().size(); maxC = (Company)u; } } catch(Exception ex){}
                }
            }
            showReport("Top Company", maxC != null ? maxC.getname() + " with " + max + " jobs." : "No data");
        });

        // 3. Applicant History
        TextField appIdTxt = new TextField(); appIdTxt.setPromptText("Applicant ID");
        Button r3 = new Button("3. View App History");
        r3.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("HISTORY FOR " + appIdTxt.getText() + ":\n----------------\n");
            for(Application1 a : Application1.applications) {
                if(a.getApplicant_id().equalsIgnoreCase(appIdTxt.getText())) {
                    sb.append("Job ID: ").append(a.getRec_id()).append(" | Status: ").append(a.get_acceptance_status()?"Accepted":"Pending").append("\n");
                }
            }
            showReport("History", sb.toString());
        });

        // 4. Max Applicant (Uses your EXISTING admin.java method)
        Button r4 = new Button("4. Top Applicant");
        r4.setOnAction(e -> {
            ArrayList<Applicant> apps = new ArrayList<>();
            for(User u : admin.getallUsers()) if(u instanceof Applicant) apps.add((Applicant)u);
            int max = adm.max_recnum_by_applicant(apps); 
            new Alert(Alert.AlertType.INFORMATION, "Max applications by one user: " + max).show();
        });

        grid.add(r1, 0, 0); grid.add(r2, 1, 0);
        grid.add(appIdTxt, 0, 1); grid.add(r3, 1, 1);
        grid.add(r4, 0, 2);

        Button logout = new Button("Logout"); logout.setOnAction(e -> showLoginScene());
        layout.getChildren().addAll(title, searchBox, userList, actionBox, new Separator(), rptLabel, grid, logout);
        window.setScene(new Scene(layout, 500, 650));
    }

    private void showReport(String title, String content) {
        Stage s = new Stage(); s.setTitle(title);
        TextArea area = new TextArea(content); area.setEditable(false);
        s.setScene(new Scene(new StackPane(area), 300, 300)); s.show();
    }

    // =====================================================================
    // 4. COMPANY DASHBOARD
    // =====================================================================
    private void showCompanyDashboard() {
        Company comp = (Company) currentUser;
        VBox layout = new VBox(10); layout.setPadding(new Insets(20));
        Label title = new Label("COMPANY: " + comp.getname()); title.setStyle("-fx-font-weight: bold;");

        ListView<recruitment> jobList = new ListView<>();
        try { jobList.getItems().addAll(comp.getRecruitments()); } catch(Exception e){}

        TextField jobTitle = new TextField(); jobTitle.setPromptText("Job Title");
        HBox jobButtons = new HBox(10);
        Button btnPost = new Button("Post"); Button btnUpdate = new Button("Update"); Button btnDelete = new Button("Delete");

        btnPost.setOnAction(e -> {
            try { recruitment r = new recruitment("REC"+System.currentTimeMillis(), jobTitle.getText(), comp.getid(), LocalDate.now());
                comp.addRecruitment(r); jobList.getItems().add(r); } catch(Exception ex) { new Alert(Alert.AlertType.ERROR, ex.getMessage()).show(); }
        });
        btnUpdate.setOnAction(e -> { recruitment r = jobList.getSelectionModel().getSelectedItem(); if(r!=null){ r.setCategory(jobTitle.getText()); comp.updateRecruitment(r); jobList.refresh();} });
        btnDelete.setOnAction(e -> { recruitment r = jobList.getSelectionModel().getSelectedItem(); if(r!=null){ try {
            comp.deleteRecruitment(r);
            } catch (Exception ex) {
                Logger.getLogger(JavaApplication9.class.getName()).log(Level.SEVERE, null, ex);
            }
jobList.getItems().remove(r);} });
        jobButtons.getChildren().addAll(btnPost, btnUpdate, btnDelete);

        // Search & Process
        Label searchLbl = new Label("1. Select Job to Auto-fill ID:");
        TextField viewId = new TextField(); viewId.setPromptText("Recruitment ID...");
        Button btnViewApps = new Button("View Applicants");
        TextArea resultArea = new TextArea(); resultArea.setEditable(false); resultArea.setPrefHeight(120);

        jobList.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{ if(n!=null) viewId.setText(n.getRec_id()); });

        btnViewApps.setOnAction(e -> refreshApplicantList(comp, viewId.getText(), resultArea));

        Label processLbl = new Label("2. Process Application (Enter Applicant ID):");
        HBox processBox = new HBox(10);
        TextField appIDInput = new TextField(); appIDInput.setPromptText("Applicant ID");
      //  ---------------
        
        
    DatePicker datePicker = new DatePicker(); 
datePicker.setPromptText("Date");
datePicker.setPrefWidth(120);

// 2. Time Input (NEW)
TextField timeField = new TextField();
timeField.setPromptText("HH:mm"); // e.g. 14:30
timeField.setPrefWidth(80);

Button btnInterview = new Button("Set Interview");
btnInterview.setOnAction(e -> {
    try {
        if (datePicker.getValue() != null && !timeField.getText().isEmpty() && !appIDInput.getText().isEmpty()) {
            
            // 3. Parse the time string (e.g., "14:30") to a LocalTime object
            LocalTime time = LocalTime.parse(timeField.getText());
            
            // 4. Combine Date + Time
            LocalDateTime interviewTime = LocalDateTime.of(datePicker.getValue(), time); 
            
            // Call backend
            comp.scheduleInterview(viewId.getText(), appIDInput.getText(), interviewTime);
            
            new Alert(Alert.AlertType.INFORMATION, "Interview Scheduled for " + interviewTime).show();
            refreshApplicantList(comp, viewId.getText(), resultArea);
            
        } else {
            new Alert(Alert.AlertType.ERROR, "Enter Applicant ID, Date, and Time (HH:mm)").show();
        }
    } catch (Exception ex) {
        new Alert(Alert.AlertType.ERROR, "Invalid Time Format! Use HH:mm (e.g. 14:30)").show();
    }
});
        
        
        Button btnAccept = new Button("Accept"); btnAccept.setStyle("-fx-background-color: #ccffcc;"); 
        Button btnReject = new Button("Reject"); btnReject.setStyle("-fx-background-color: #ffcccc;"); 

        btnAccept.setOnAction(e -> updateStatus(viewId.getText(), appIDInput.getText(), true, comp, resultArea));
        btnReject.setOnAction(e -> updateStatus(viewId.getText(), appIDInput.getText(), false, comp, resultArea));
// Update this specific line to include your new controls:
processBox.getChildren().addAll(appIDInput, datePicker, timeField, btnInterview, btnAccept, btnReject);        // Stats
        HBox statsBox = new HBox(10);
        DatePicker startP = new DatePicker(); DatePicker endP = new DatePicker();
        
        // In showCompanyDashboard ...
Button btnReport = new Button("Generate Stats");
btnReport.setOnAction(e -> {
    if (startP.getValue() != null && endP.getValue() != null) {
        
        // 1. Get Total Jobs Posted (Existing)
        int total = comp.getTotalRecruitments(startP.getValue(), endP.getValue());
        
        // 2. Get Most Popular Job (NEW)
        String mostPopular = comp.mostAppliedJob(startP.getValue(), endP.getValue());
        
        // 3. Show both in the Alert
        String message = "STATISTICS REPORT\n" +
                         "From: " + startP.getValue() + "  To: " + endP.getValue() + "\n" +
                         "------------------------------------------------\n" +
                         "1. Total Jobs Posted: " + total + "\n" +
                         "2. Most Popular Job:  " + mostPopular;
                         
        new Alert(Alert.AlertType.INFORMATION, message).show();
        
    } else {
        new Alert(Alert.AlertType.ERROR, "Please select Start and End dates.").show();
    }
});
        
        
        
        
        
        statsBox.getChildren().addAll(startP, endP, btnReport);

        Button logout = new Button("Logout"); logout.setOnAction(e -> showLoginScene());
        layout.getChildren().addAll(title, jobTitle, jobButtons, jobList, new Separator(), searchLbl, new HBox(10, viewId, btnViewApps), resultArea, processLbl, processBox, new Separator(), new Label("Reports:"), statsBox, logout);
        window.setScene(new Scene(layout, 450, 750));
    }

    // In JavaApplication9.java

private void refreshApplicantList(Company comp, String targetId, TextArea area) {
    // This prints to console (Backend requirement)
    comp.viewApplicantsForRecruitment(targetId); 
    
    StringBuilder sb = new StringBuilder("Applicants for REC: " + targetId + "\n--------------------\n");
    boolean found = false;
    
    for(Application1 app : Application1.applications) {
        if(app.getRec_id().equals(targetId)) {
            found = true;
            
            // 1. Basic Info & Status
            sb.append("Applicant ID: ").append(app.getApplicant_id())
              .append(" | Status: ").append(app.get_acceptance_status() ? "[ACCEPTED]" : "[Pending]");
            
            // 2. SHOW INTERVIEW TIME (If set)
            if(app.get_interview_timing() != null) {
                // .toString() gives "2023-12-01T14:30". We replace 'T' with a space for looks.
                sb.append(" | Interview: ").append(app.get_interview_timing().toString().replace("T", " "));
            }
            
            // 3. CV
            sb.append("\nCV: ").append(app.get_cv()).append("\n\n");
        }
    }
    
    if(!found) sb.append("No applicants found.");
    area.setText(sb.toString());
}

    private void updateStatus(String recId, String applicantId, boolean status, Company comp, TextArea area) {
        boolean found = false;
        for(Application1 app : Application1.applications) {
            if(app.getRec_id().equals(recId) && app.getApplicant_id().equals(applicantId)) {
                app.set_acceptance_status(status);
                found = true;
                new Alert(Alert.AlertType.INFORMATION, "Status Updated").show();
                refreshApplicantList(comp, recId, area);
                break;
            }
        }
        if(!found) new Alert(Alert.AlertType.ERROR, "ID not found.").show();
    }

    // =====================================================================
    // 5. APPLICANT DASHBOARD
    // =====================================================================
    private void showApplicantDashboard() {
        Applicant app = (Applicant) currentUser;
        VBox layout = new VBox(10); layout.setPadding(new Insets(20));
        Label title = new Label("APPLICANT: " + app.getname()); title.setStyle("-fx-font-weight: bold;");

        TextArea cvArea = new TextArea(); cvArea.setText(app.getCv()); cvArea.setPrefHeight(80);
        Button btnSaveCV = new Button("Save CV Draft");
        btnSaveCV.setOnAction(e -> { app.setCv(cvArea.getText()); new Alert(Alert.AlertType.INFORMATION, "Saved").show(); });

        Button btnApply = new Button("Browse Jobs & Apply");
        btnApply.setOnAction(e -> {
            ListView<recruitment> jobs = new ListView<>();
            for(User u : admin.getallUsers()) if(u instanceof Company) {
                try { jobs.getItems().addAll(((Company)u).getRecruitments()); } catch(Exception ex){}
            }
            Button confirm = new Button("Apply");
            confirm.setOnAction(ev -> {
                recruitment r = jobs.getSelectionModel().getSelectedItem();
                if(r != null) {
                    try {
                        Application1 a = new Application1(app.getid(), r.getRec_id(), "APP"+System.currentTimeMillis(), app.getCv(), LocalDate.now(), null, false);
                        app.applyforjob(a, r); 
                        new Alert(Alert.AlertType.INFORMATION, "Applied!").show();
                    } catch(Exception ex) { new Alert(Alert.AlertType.ERROR, ex.getMessage()).show(); }
                }
            });
            Stage s = new Stage(); s.setScene(new Scene(new VBox(10, jobs, confirm), 300, 400)); s.show();
        });

        HBox searchAppBox = new HBox(10);
        TextField appIdField = new TextField(); appIdField.setPromptText("App ID to Search");
        Button btnSearchApp = new Button("Find App");
   
        
        
        // Inside showApplicantDashboard...

btnSearchApp.setOnAction(e -> {
    try {
        Application1 target = null;
        // Find the application by ID
        for(Application1 a : Application1.applications) {
            if(a.get_application_id().equals(appIdField.getText())) target = a;
        }

        if(target != null) {
            // Build the message
            String interviewInfo = (target.get_interview_timing() != null) 
                                   ? target.get_interview_timing().toString().replace("T", " ") 
                                   : "Not Set";

            String status = target.get_acceptance_status() ? "Accepted" : "Pending";

            String msg = "Job ID: " + target.getRec_id() + 
                         "\nStatus: " + status + 
                         "\nInterview Date: " + interviewInfo;

            new Alert(Alert.AlertType.INFORMATION, msg).show();
        } else {
            throw new Exception("Application ID not found");
        }
    } catch (Exception ex) { 
        new Alert(Alert.AlertType.ERROR, ex.getMessage()).show(); 
    }
});
        
        
        
        
        
        searchAppBox.getChildren().addAll(appIdField, btnSearchApp);

        Button btnStatus = new Button("View All Statuses");
// Inside showApplicantDashboard...

btnStatus.setOnAction(e -> { 
    StringBuilder sb = new StringBuilder("MY APPLICATION HISTORY:\n----------------------\n");
    boolean found = false;

    for(Application1 a : Application1.applications) {
        // Only show applications for THIS applicant
        if(a.getApplicant_id().equals(app.getid())) {
            found = true;
            
            String interviewInfo = (a.get_interview_timing() != null) 
                                   ? a.get_interview_timing().toString().replace("T", " ") 
                                   : "Not Set";
            
            sb.append("App ID: ").append(a.get_application_id())
              .append("\nJob ID: ").append(a.getRec_id())
              .append("\nStatus: ").append(a.get_acceptance_status() ? "Accepted" : "Pending")
              .append("\nInterview: ").append(interviewInfo)
              .append("\n----------------------\n");
        }
    }

    if(!found) sb.append("No applications submitted yet.");

    // Show in a scrollable text window
    TextArea area = new TextArea(sb.toString()); 
    area.setEditable(false);
    Stage s = new Stage(); 
    s.setTitle("My Statuses");
    s.setScene(new Scene(new StackPane(area), 400, 500)); 
    s.show();
});
        
        
        
        Button logout = new Button("Logout"); logout.setOnAction(e -> showLoginScene());
        layout.getChildren().addAll(title, new Label("CV:"), cvArea, btnSaveCV, new Separator(), btnApply, new Label("Search My Apps:"), searchAppBox, btnStatus, logout);
        window.setScene(new Scene(layout, 400, 600));
    }

    public static void main(String[] args) { launch(args); }
}