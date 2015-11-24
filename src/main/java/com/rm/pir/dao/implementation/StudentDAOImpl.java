package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Student;
import com.rm.pir.utilities.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class StudentDAOImpl extends DBManager implements StudentDAO {
 
//    private Connection con;
//    
//    public void open() throws SQLException {
//        con = super.getConnection();
//    }
//
//    public void close() throws SQLException {
//        con.close();
//    }
    
    @Override
    public long insert(Student student) throws SQLException {
        long key;
        try (Connection con = super.getConnection(); 
                PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO normal_partner.student "
                                + "(email, lastname, firstname, college, gender, "
                                + "homephone, cellphone, first_time, ortn_complete, "
                                + "bckgrnd_check_complete, notes, is_spec_ed, is_lang_ed, two_chldn) "
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, student.getEmail());
            insert.setString(2, Util.sanitize(student.getLastname()));
            insert.setString(3, Util.sanitize(student.getFirstname()));
            insert.setString(4, Util.sanitize(student.getCollege()));
            insert.setString(5, student.getGender().toLowerCase());
            insert.setString(6, student.getHomephone());
            insert.setString(7, student.getCellphone());
            insert.setBoolean(8, student.isFirst_time());
            insert.setBoolean(9, student.isOrtn_complete());
            insert.setBoolean(10, student.isBckgrnd_check_complete());
            insert.setString(11, Util.sanitize(student.getNotes()));
            insert.setBoolean(12, student.isSpec_ed());
            insert.setBoolean(13, student.isLang_ed());
            insert.setBoolean(14, student.isTwo_chldn());
            insert.executeUpdate();
            key = 0;
            try (ResultSet rs = insert.getGeneratedKeys()) {
                if (rs.next())
                    key = rs.getLong(1);
            }
        }
        
        return key;
    }

    @Override
    public int update(Student student) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(
                                "UPDATE normal_partner.student s "
                                + "SET s.email = ?, "
                                + "s.lastname = ?,"
                                + "s.firstname = ?,"
                                + "s.college = ?,"
                                + "s.gender = ?,"
                                + "s.homephone = ?,"
                                + "s.cellphone = ?,"
                                + "s.first_time = ?,"
                                + "s.ortn_complete = ?,"
                                + "s.bckgrnd_check_complete = ?,"
                                + "s.notes = ?,"
                                + "s.is_spec_ed = ?,"
                                + "s.is_lang_ed = ?, "
                                + "s.two_chldn = ? "
                                + "WHERE s.studentid = ?")) {
            update.setString(1, student.getEmail());
            update.setString(2, student.getLastname());
            update.setString(3, student.getFirstname());
            update.setString(4, student.getCollege());
            update.setString(5, student.getGender());
            update.setString(6, student.getHomephone());
            update.setString(7, student.getCellphone());
            update.setBoolean(8, student.isFirst_time());
            update.setBoolean(9, student.isOrtn_complete());
            update.setBoolean(10, student.isBckgrnd_check_complete());
            update.setString(11, student.getNotes());
            update.setBoolean(12, student.isSpec_ed());
            update.setBoolean(13, student.isLang_ed());
            update.setBoolean(14, student.isTwo_chldn());
            update.setLong(15, student.getStudentID());
            result = update.executeUpdate();
        }
        
        return result;  
    }

    @Override
    public int delete(Student student) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement delete = con.prepareStatement(
                                "DELETE FROM normal_partner.student "
                                + "WHERE studentid = ?")) {
            delete.setLong(1, student.getStudentID());
            result = delete.executeUpdate();
        }
        
        return result;
    }

    @Override
    public Student findByID(long studentID) throws SQLException {
        Student bean = null;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT * FROM normal_partner.student s "
                                + "WHERE s.studentid = ?")) {
            find.setLong(1, studentID);
            try (ResultSet rs = find.executeQuery()) {
                if (rs.next()) {
                    bean = new Student();
                    bean.setStudentID(rs.getLong("studentid"));
                    bean.setEmail(rs.getString("email"));
                    bean.setLastname(rs.getString("lastname"));
                    bean.setFirstname(rs.getString("firstname"));
                    bean.setCollege(rs.getString("college"));
                    bean.setGender(rs.getString("gender"));
                    bean.setHomephone(rs.getString("homephone"));
                    bean.setCellphone(rs.getString("cellphone"));
                    bean.setFirst_time(rs.getBoolean("first_time"));
                    bean.setOrtn_complete(rs.getBoolean("ortn_complete"));
                    bean.setBckgrnd_check_complete(rs.getBoolean("bckgrnd_check_complete"));
                    bean.setNotes(rs.getString("notes"));
                    bean.setSpec_ed(rs.getBoolean("is_spec_ed"));
                    bean.setLang_ed(rs.getBoolean("is_lang_ed"));
                    bean.setTwo_chldn(rs.getBoolean("two_chldn")); 
                }
            }
        }
        
        return bean;
    }

    @Override
    public Student findByEmail(String email) throws SQLException {
        Student bean = null;
        try (Connection con = super.getConnection(); 
                PreparedStatement find = con.prepareStatement(
                                "SELECT * FROM normal_partner.student s "
                                + "WHERE s.email = ?")) {
            find.setString(1, email);
            try (ResultSet rs = find.executeQuery()) {
                if (rs.next()) {
                    bean = new Student();
                    bean.setStudentID(rs.getLong("studentid"));
                    bean.setEmail(rs.getString("email"));
                    bean.setLastname(rs.getString("lastname"));
                    bean.setFirstname(rs.getString("firstname"));
                    bean.setCollege(rs.getString("college"));
                    bean.setGender(rs.getString("gender"));
                    bean.setHomephone(rs.getString("homephone"));
                    bean.setCellphone(rs.getString("cellphone"));
                    bean.setFirst_time(rs.getBoolean("first_time"));
                    bean.setOrtn_complete(rs.getBoolean("ortn_complete"));
                    bean.setBckgrnd_check_complete(rs.getBoolean("bckgrnd_check_complete"));
                    bean.setNotes(rs.getString("notes"));
                    bean.setSpec_ed(rs.getBoolean("is_spec_ed"));
                    bean.setLang_ed(rs.getBoolean("is_lang_ed"));
                    bean.setTwo_chldn(rs.getBoolean("two_chldn"));
                }
            }
        }
        
        return bean;
    }

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        try (Connection con = super.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM normal_partner.student "
                        + "ORDER BY lastname, firstname"); 
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Student student = new Student();
                student.setStudentID(rs.getLong("studentid"));
                student.setEmail(rs.getString("email"));
                student.setLastname(rs.getString("lastname"));
                student.setFirstname(rs.getString("firstname"));
                student.setCollege(rs.getString("college"));
                student.setGender(rs.getString("gender"));
                student.setHomephone(rs.getString("homephone"));
                student.setCellphone(rs.getString("cellphone"));
                student.setFirst_time(rs.getBoolean("first_time"));
                student.setOrtn_complete(rs.getBoolean("ortn_complete"));
                student.setBckgrnd_check_complete(rs.getBoolean("bckgrnd_check_complete"));
                student.setNotes(rs.getString("notes"));
                student.setSpec_ed(rs.getBoolean("is_spec_ed"));
                student.setLang_ed(rs.getBoolean("is_lang_ed"));
                student.setTwo_chldn(rs.getBoolean("two_chldn"));
                list.add(student);
            }
        }
        return list;
    }
    
    @Override
    public List<String> findAllAvailableStudentEmails() throws SQLException {
        List<String> emails;
        try (Connection con = super.getConnection(); 
                PreparedStatement query = con.prepareStatement(
                                "SELECT email "
                                + "FROM normal_partner.student "
                                + "WHERE studentid IN "
                                + "(SELECT studentid FROM normal_partner.availability_student GROUP BY studentid)"); 
                ResultSet rs = query.executeQuery()) {
            emails = new ArrayList<>();
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        }
        
        return emails;
    }

    @Override
    public int updateOrientation(Student student) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(
                                "UPDATE normal_partner.student s "
                                + "SET s.ortn_complete = ?, "
                                + "s.first_time = ? "
                                + "WHERE s.studentid = ?")) {
            update.setBoolean(1, student.isOrtn_complete());
            update.setBoolean(2, student.isFirst_time());
            update.setLong(3, student.getStudentID());
            result = update.executeUpdate();
        }
        
        return result;
    }
    
    @Override
    public int updateBackgroundCheck(Student student) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement update = con.prepareStatement(
                                "UPDATE normal_partner.student s "
                                + "SET s.bckgrnd_check_complete = ? "
                                + "WHERE s.studentid = ?")) {
            update.setBoolean(1, student.isBckgrnd_check_complete());
            update.setLong(2, student.getStudentID());
            result = update.executeUpdate();
        }
        
        return result;
    }
}
