/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Student;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface StudentAvailDAO {
    
    public int insert(Student student) throws SQLException;
    public int update(Student student) throws SQLException;
    public int delete(Student student) throws SQLException;
    public Map<String, List<String>> findByID(long studentID) throws SQLException;
}
