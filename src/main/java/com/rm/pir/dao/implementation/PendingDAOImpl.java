package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.PairingDAO;
import com.rm.pir.dao.interfaces.PendingDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Session;
import com.rm.pir.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class PendingDAOImpl extends DBManager implements PendingDAO {

//    private Connection con;
    @Inject 
    private StudentDAO sdao;
    @Inject
    private ChildDAO cdao;
    @Inject
    private PairingDAO pdao;
    
//    public void open() throws SQLException {
//        con = super.getConnection();
//    }
//
//    public void close() throws SQLException {
//        con.close();
//    }
    
    @Override
    public List<Session> findAll() throws SQLException {
        List<Session> list;
        try (Connection con = super.getConnection()) {
            list = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.pending"); 
                    ResultSet rs = find.executeQuery()) {
                while (rs.next()) {
                    long s = rs.getLong("studentid");
                    Student student = sdao.findByID(s);
                    long c = rs.getLong("childid");
                    Child child = cdao.findByID(c);
                    String day = rs.getString("session_day");
                    String hour = rs.getString("session_hour");
                    list.add(new Session(student, child, day, hour));
                }
            }
        }
        
        return list;
    }

    @Override
    public int delete(Session session) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement delete = con.prepareStatement(
                                "DELETE FROM normal_partner.pending "
                                + "WHERE studentid= ? "
                                + "AND childid= ?")) {
            delete.setLong(1, session.getStudent().getStudentID());
            delete.setLong(2, session.getChild().getChildID());
            result = delete.executeUpdate();
        }

        return result;
    }

    @Override
    public int transfer(Session session) throws SQLException {
        delete(session);
        return pdao.insert(session);
    }

    @Override
    public List<Session> findByStudent(Student student) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        try(Connection con = super.getConnection(); 
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM normal_partner.pending WHERE studentid = ?")) {
            ps.setLong(1, student.getStudentID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = sdao.findByID(rs.getLong("studentid"));
                    Child c = cdao.findByID(rs.getLong("childid"));
                    String day = rs.getString("session_day");
                    String hour = rs.getString("session_hour");
                    sessions.add(new Session(s,c,day,hour));
                }
            }
        }
        return sessions;
    }
}
