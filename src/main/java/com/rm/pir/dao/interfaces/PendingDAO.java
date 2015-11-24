package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Session;
import com.rm.pir.model.Student;
import java.sql.SQLException;
import java.util.List;


public interface PendingDAO {
    
    public List<Session> findAll() throws SQLException;
    public List<Session> findByStudent(Student student) throws SQLException;
    public int delete(Session session) throws SQLException;
    public int transfer(Session session) throws SQLException;
}
