/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.model;

import javax.inject.Named;

@Named
public class AttendanceReport {
    
    private Student student;
    private int count;

    public AttendanceReport() {
        student = null;
        count = -1;
    }
    
    public AttendanceReport(Student student, int count) {
        this.student = student;
        this.count = count;
    }
    
    /**
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }
    
}
