package com.example;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JComboBox;

class Doctor {
    private String dName;
    

    public Doctor(String dName) {
        this.dName = dName;
        
    }

    public String getDName() {
        return dName;
    }
    public static ArrayList<Doctor> getDoctorList() {
        ArrayList<Doctor> doctorList = new ArrayList<>();
        return doctorList;
    }
    
    
}