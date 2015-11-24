/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.model;

import javax.inject.Named;

@Named
public class Session {
    private Student student;
    private Child child;
    private String day;
    private String hour;
    
    public Session(){
        student = null;
        child = null;
        day = null;
        hour = null;
    }
    
    public Session(Student student, Child child, String day, String hour) {
        this.student = student;
        this.child = child;
        this.day = day;
        this.hour = hour;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Session) {
            Session temp = (Session) o;
            if (this.student.getStudentID() == temp.getStudent().getStudentID())
                if (this.child.getChildID() == temp.getChild().getChildID())
                    if (this.day.equals(temp.getDay()))
                        if (this.hour.equals(temp.getHour()))
                            return true;
        }
        return false;
    }
}

