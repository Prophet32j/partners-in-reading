package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.PairingDAO;
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
public class PairingDAOImpl extends DBManager implements PairingDAO {
    
//    private Connection con;
    @Inject 
    private StudentDAO sdao;
    @Inject
    private ChildDAO cdao;
    
//    public void open() throws SQLException {
//        con = super.getConnection();
//    }
//
//    public void close() throws SQLException {
//        con.close();
//    }

    @Override
    public int insert(Session session) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO normal_partner.pairs "
                                + "(studentid, childid, session_day, session_hour) "
                                + "VALUES (?,?,?,?)")) {
            insert.setLong(1, session.getStudent().getStudentID());
            insert.setLong(2, session.getChild().getChildID());
            insert.setString(3, session.getDay());
            insert.setString(4, session.getHour());
            result = insert.executeUpdate();
        }
        
        return result;
    }

    @Override
    public int update(Session session) throws SQLException {
        delete(session);
        return insert(session);
    }

    @Override
    public int delete(Session session) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement delete = con.prepareStatement(
                                "DELETE FROM normal_partner.pairs "
                                + "WHERE studentid= ? "
                                + "AND childid= ?")) {
            delete.setLong(1, session.getStudent().getStudentID());
            delete.setLong(2, session.getChild().getChildID());
            result = delete.executeUpdate();
        }
        
        return result;
    }

    @Override
    public List<Session> findAll() throws SQLException {
        List<Session> list;
        try (Connection con = super.getConnection()) {
            list = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.pairs"); 
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
    public List<Session> findByDay(String weekday) throws SQLException {
        List<Session> list;
        try (Connection con = super.getConnection()) {
            list = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                         "SELECT * FROM normal_partner.pairs p "
                         + "WHERE p.session_day = ?")) {
                ps.setString(1, weekday);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // get all Session elements
                        long studentid = rs.getLong("studentid");
                        long childid = rs.getLong("childid");
                        String day = rs.getString("session_day");
                        String hour = rs.getString("session_hour");
                        // populate Student and Child objects
                        Student student = sdao.findByID(studentid);
                        Child child = cdao.findByID(childid);
                        
                        list.add(new Session(student, child, day, hour));
                    }
                }
            }
        }
        
        return list;
    }

    @Override
    public List<Session> search(String search) throws SQLException {
        List<Session> list;
        try (Connection con = super.getConnection()) {
            String sql = 
                    "SELECT * FROM normal_partner.pairs p WHERE p.studentid IN "
                    + "(SELECT studentid FROM normal_partner.student s WHERE s.firstname LIKE ? OR s.lastname LIKE ?) "
                    + "OR p.childid IN "
                    + "(SELECT childid FROM normal_partner.child c WHERE c.firstname LIKE ? OR c.lastname LIKE ?)";
            search = "%" + search + "%";
            list = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (int i=1; i<5; i++)
                    ps.setString(i, search);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // get all Session elements
                        long studentid = rs.getLong("studentid");
                        long childid = rs.getLong("childid");
                        String day = rs.getString("session_day");
                        String hour = rs.getString("session_hour");
                        // populate Student and Child objects
                        Student student = sdao.findByID(studentid);
                        Child child = cdao.findByID(childid);
                        list.add(new Session(student, child, day, hour));
                    }
                }
            }
        }
        
        return list;
    }

    @Override
    public List<Session> findByStudent(Student student) throws SQLException {
        List<Session> sessions;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT * FROM normal_partner.pairs p "
                                + "WHERE p.studentid = ?")) {
            find.setLong(1, student.getStudentID());
            try (ResultSet rs = find.executeQuery()) {
                sessions = new ArrayList<>();
                while (rs.next()) {
                    String day = rs.getString("session_day");
                    String hour = rs.getString("session_hour");
                    Child child = cdao.findByID(rs.getLong("childid"));
                    sessions.add(new Session(student, child, day, hour));
                }
            }
        }
        
        return sessions;
    }

    @Override
    public Session findByChild(Child child) throws SQLException {
        Session session;
        try (Connection con = super.getConnection()) {
            session = null;
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.pairs p "
                         + "WHERE p.childid = ?")) {
                find.setLong(1, child.getChildID());
                try (ResultSet rs = find.executeQuery()) {
                    if (rs.next()) {
                        String day = rs.getString("session_day");
                        String hour = rs.getString("session_hour");
                        Student student = sdao.findByID(rs.getLong("studentid"));
                        session = new Session(student,child,day,hour);
                    }
                }
            }
        }
        
        return session;
    }
}
