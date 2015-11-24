/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Admin;
import java.sql.SQLException;

public interface AdminDAO {
    
    public int insert(Admin admin) throws SQLException;
    public int update(Admin admin) throws SQLException;
    public int delete(Admin admin) throws SQLException;
    public Admin findByID(long adminID) throws SQLException;
    public Admin findByEmail(String email) throws SQLException;
    public Admin[] findAll() throws SQLException;
}
