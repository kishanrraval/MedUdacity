package com.example.krish.medical_app.Java_classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by parini on 16-06-2017.
 */

public class Patient
{
    private String patient_id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String department;
    private String gender;
    private String dob;
    private String age;
    private String email;
    private String address;
    private String mobile;
    private String phone;
    private ArrayList<String> diagnosis_list;
    private String medical_history;
    protected DatabaseReference patient = FirebaseDatabase.getInstance().getReference();

    public Patient(String patient_id, String first_name, String middle_name, String last_name,String department, String gender, String dob,
            String age, String email, String address, String mobile, String phone, ArrayList<String> diagnosis_list, String medical_history) {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.patient_id = patient_id;
        this.gender = gender;
        this.dob = dob;
        this.age = age;
        this.email = email;
        this.address = address;
        this.mobile = mobile;
        this.phone = phone;
        this.medical_history = medical_history;
        this.department = department;
        this.diagnosis_list = diagnosis_list;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMedical_history() {
        return medical_history;
    }

    public void setMedical_history(String medical_history) {
        this.medical_history = medical_history;
    }

    public void firebase_connect(String doc_username)
    {

        Map<String,Object> map_patient = new HashMap<String,Object>();
        map_patient.put("patient_first_name",first_name);
        map_patient.put("patient_middle_name",middle_name);
        map_patient.put("patient_last_name",last_name);
        map_patient.put("patient_department",department);
        map_patient.put("patient_gender",gender);
        map_patient.put("patient_dob",dob);
        map_patient.put("patient_email",email);
        map_patient.put("patient_address",address);
        map_patient.put("patient_mobile",mobile);
        map_patient.put("patient_phone",phone);
        map_patient.put("patient_medical_history",medical_history);

        Map<String,Object>map_diagnosis = new HashMap<String, Object>();
        for(int i = 0; i<diagnosis_list.size();i++)
        {
            String id = patient.child(doc_username).child("patients").child(patient_id).child("patien_diagnosis").push().getKey();
            map_diagnosis.put(id,diagnosis_list.get(i));
        }

        patient.child(doc_username).child("patients").child(patient_id).updateChildren(map_patient);
        patient.child(doc_username).child("patients").child(patient_id).child("patient_diagnosis").setValue(map_diagnosis);
    }
}

