/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Child;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ChildAvailDAO {
    
    public int insert(Child child) throws SQLException;
    public int update(Child child) throws SQLException;
    public int delete(Child child) throws SQLException;
    public Map<String, List<String>> findByID(long childID) throws SQLException;
}
