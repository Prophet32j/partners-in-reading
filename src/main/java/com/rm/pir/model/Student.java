/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class Student implements Serializable {

    private long studentID;
    private String email;
    private String lastname;
    private String firstname;
    private String college;
    private String gender;
    private String homephone;
    private String cellphone;
    private boolean spec_ed;
    private boolean lang_ed;
    private boolean first_time;
    private boolean ortn_complete;
    private boolean bckgrnd_check_complete;
    private String notes;
    private Map<String, List<String>> daymap;
    private boolean two_chldn;

    public Student() {
    }
    
    public Student(Student student) {
        copyStudent(student);
    }
    
    public void copyStudent(Student student) {
        this.studentID = student.studentID;
        this.email = student.email;
        this.lastname = student.lastname;
        this.firstname = student.firstname;
        this.college = student.college;
        this.gender = student.gender;
        this.homephone = student.homephone;
        this.cellphone = student.cellphone;
        this.spec_ed = student.spec_ed;
        this.lang_ed = student.lang_ed;
        this.first_time = student.first_time;
        this.ortn_complete = student.ortn_complete;
        this.bckgrnd_check_complete = student.bckgrnd_check_complete;
        this.notes = student.notes;
        this.two_chldn = student.two_chldn;
        if (student.daymap != null)
            copyMap(student.daymap);
    }
    
    private void copyMap(Map<String, List<String>> map) {
        this.daymap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            //get the key and values and set them, looping on the list
            List<String> value = entry.getValue();
            String key = entry.getKey();
            daymap.put(key, new ArrayList<>(value));
        }
    }
    
    public long getStudentID() {
        return studentID;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomephone() {
        return homephone;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<String, List<String>> getDaymap() {
        return daymap;
    }

    public void setDaymap(Map<String, List<String>> daymap) {
        this.daymap = daymap;
    }

    /**
     * @return the first_time
     */
    public boolean isFirst_time() {
        return first_time;
    }

    /**
     * @param first_time the first_time to set
     */
    public void setFirst_time(boolean first_time) {
        this.first_time = first_time;
    }

    /**
     * @return the ortn_complete
     */
    public boolean isOrtn_complete() {
        return ortn_complete;
    }

    /**
     * @param ortn_complete the ortn_complete to set
     */
    public void setOrtn_complete(boolean ortn_complete) {
        this.ortn_complete = ortn_complete;
    }

    /**
     * @return the bckgrnd_check_complete
     */
    public boolean isBckgrnd_check_complete() {
        return bckgrnd_check_complete;
    }

    /**
     * @param bckgrnd_check_complete the bckgrnd_check_complete to set
     */
    public void setBckgrnd_check_complete(boolean bckgrnd_check_complete) {
        this.bckgrnd_check_complete = bckgrnd_check_complete;
    }

    /**
     * @return the is_spec_ed
     */
    public boolean isSpec_ed() {
        return spec_ed;
    }

    /**
     * @param is_spec_ed the is_spec_ed to set
     */
    public void setSpec_ed(boolean spec_ed) {
        this.spec_ed = spec_ed;
    }

    /**
     * @return the is_lang_ed
     */
    public boolean isLang_ed() {
        return lang_ed;
    }

    /**
     * @param is_lang_ed the is_lang_ed to set
     */
    public void setLang_ed(boolean lang_ed) {
        this.lang_ed = lang_ed;
    }

    /**
     * @return the two_chldn
     */
    public boolean isTwo_chldn() {
        return two_chldn;
    }

    /**
     * @param two_chldn the two_chldn to set
     */
    public void setTwo_chldn(boolean two_chldn) {
        this.two_chldn = two_chldn;
    }
}
