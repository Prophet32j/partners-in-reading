package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Child;
import com.rm.pir.model.Session;
import com.rm.pir.model.Student;
import java.sql.SQLException;
import java.util.List;

public interface PairingDAO {
    
    public int insert(Session session) throws SQLException;
    public int update(Session session) throws SQLException;
    public int delete(Session session) throws SQLException;
    public List<Session> findAll() throws SQLException;
    public List<Session> findByDay(String weekday) throws SQLException;
    public List<Session> findByStudent(Student student) throws SQLException;
    public Session findByChild(Child child) throws SQLException;
    public List<Session> search(String search) throws SQLException;
}
