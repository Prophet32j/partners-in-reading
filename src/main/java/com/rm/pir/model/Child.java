/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;

@Named
public class Child {
    
    private long childID;
    private String email;
    private String lastname;
    private String firstname;
    private String gender;
    private int age;
    private String grade;
    private String homephone;
    private String cellphone;
    private boolean special_needs;
    private boolean language_needs;
    private String parent_one;
    private String parent_two;
    private String notes;
    private Map<String, List<String>> daymap;

    public Child() {
        
    }
    
    public Child(Child child) {
        this.childID = child.childID;
        this.email = child.email;
        this.lastname = child.lastname;
        this.firstname = child.firstname;
        this.gender = child.gender;
        this.age = child.age;
        this.grade = child.grade;
        this.homephone = child.homephone;
        this.cellphone = child.cellphone;
        this.special_needs = child.special_needs;
        this.language_needs = child.language_needs;
        this.parent_one = child.parent_one;
        this.parent_two = child.parent_two;
        this.notes = child.notes;
        copyMap(child.daymap);
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
    
    @Override
    public boolean equals(Object o) {
        Child child = (Child) o;
        return child.getChildID() == this.childID;
    }
    
    public long getChildID() {
        return childID;
    }

    public void setChildID(long childID) {
        this.childID = childID;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public boolean isSpecial_needs() {
        return special_needs;
    }

    public void setSpecial_needs(boolean special_needs) {
        this.special_needs = special_needs;
    }

    public String getParent_one() {
        return parent_one;
    }

    public void setParent_one(String parent_one) {
        this.parent_one = parent_one;
    }

    public String getParent_two() {
        return parent_two;
    }

    public void setParent_two(String parent_two) {
        this.parent_two = parent_two;
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
     * @return the language_needs
     */
    public boolean isLanguage_needs() {
        return language_needs;
    }

    /**
     * @param language_needs the language_needs to set
     */
    public void setLanguage_needs(boolean language_needs) {
        this.language_needs = language_needs;
    }
}
