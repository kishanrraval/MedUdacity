package com.example.krish.medical_app.Java_classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by parini on 16-06-2017.
 */

public class Note
{
    private String notes_id;
    private String title;
    private String date;
    private String text;
    private String medication;
    private String dispense;
    private String unit;
    private String refills;
    private String sig;
    private DatabaseReference doc_id = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference note;

    public Note(String notes_id,String title, String date, String text,String medication, String dispense, String unit, String refills, String sig)
    {
        this.title = title;
        this.notes_id = notes_id;
        this.date = date;
        this.text = text;
        this.medication=medication;
        this.dispense=dispense;
        this.unit = unit;
        this.refills = refills;
        this.sig=sig;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(String notes_id) {
        this.notes_id = notes_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDispense() {
        return dispense;
    }

    public void setDispense(String dispense) {
        this.dispense = dispense;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRefills() {
        return refills;
    }

    public void setRefills(String refills) {
        this.refills = refills;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public void firebase_note(String doc_username,String pat_id)
    {
        note = doc_id.child(doc_username).child("patients").child(pat_id).child("notes");
        Map<String,Object> map_note = new HashMap<String, Object>();
        map_note.put("note_title",title);
        map_note.put("note_date",date);
        map_note.put("note_text",text);
        map_note.put("note_medication",medication);
        map_note.put("note_dispense",dispense);
        map_note.put("note_unit",unit);
        map_note.put("note_refills",refills);
        map_note.put("note_sig",sig);

        note.child(notes_id).updateChildren(map_note);
    }
}
