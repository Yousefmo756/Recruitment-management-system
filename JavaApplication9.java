/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication9;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    private static final String FILE_NAME = "system_data.dat";

    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(admin.getallUsers());
            oos.writeObject(Application1.applications);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            ArrayList<Application1> apps = (ArrayList<Application1>) ois.readObject();
            
            admin.getallUsers().clear();
            admin.getallUsers().addAll(users);
            Application1.applications.clear();
            Application1.applications.addAll(apps);
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}