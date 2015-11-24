package com.rm.pir.model;

public class Partner {

    private Student student;
    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    
    public Partner() {
    }

    public Partner(Student student, long id, String firstname, String lastname, String phone) {
        this.student = student;
        this.id = id;
        this.firstName = firstname;
        this.lastName = lastname;
        this.phone = phone;
    }
    
    public Partner(Student student, String firstname, String lastname, String phone) {
        this.student = student;
        this.firstName = firstname;
        this.lastName = lastname;
        this.phone = phone;
    }
    /**
     * @return the childid
     */
    public long getID() {
        return id;
    }

    /**
     * @param id the childid to set
     */
    public void setID(long id) {
        this.id = id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
}
