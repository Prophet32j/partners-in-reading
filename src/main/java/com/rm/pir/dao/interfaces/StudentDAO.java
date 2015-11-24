/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Student;
import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {
    
    public long insert(Student student) throws SQLException;
    public int update(Student student) throws SQLException;
    public int delete(Student student) throws SQLException;
    public Student findByID(long studentID) throws SQLException;
    public Student findByEmail(String email) throws SQLException;
    public List<Student> findAll() throws SQLException;
    public List<String> findAllAvailableStudentEmails() throws SQLException;
   
    public int updateOrientation(Student student) throws SQLException;
    public int updateBackgroundCheck(Student student) throws SQLException;
    
//    public int changeCell(Student student) throws SQLException;
//    public int changeHome(Student student) throws SQLException;
//    public int changeNotes(Student student) throws SQLException;
//    public int changeFName(Student student) throws SQLException;
//    public int changeLName(Student student) throws SQLException;
//    public int changeCollege(Student student) throws SQLException;
}
