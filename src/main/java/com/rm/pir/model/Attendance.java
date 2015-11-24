package com.rm.pir.model;

import javax.inject.Named;
import org.joda.time.DateTime;

@Named
public class Attendance {
    
    private Student student;
    private DateTime timestamp;

    public Attendance() {
        student = null;
        timestamp = null;
    }
    
    public Attendance(Student student, DateTime timestamp) {
        this.student = student;
        this.timestamp = timestamp;
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
     * @return the timestamp
     */
    public DateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }
}
