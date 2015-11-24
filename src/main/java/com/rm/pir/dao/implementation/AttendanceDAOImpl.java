package com.rm.pir.dao.implementation;

import com.rm.pir.dao.interfaces.AttendanceDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Attendance;
import com.rm.pir.model.AttendanceReport;
import com.rm.pir.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.joda.time.DateTime;

@Named
@RequestScoped
public class AttendanceDAOImpl extends DBManager implements AttendanceDAO {
 
    @Inject
    private StudentDAO sdao;
    
    @Override
    public int takeAttendance(long studentid) throws SQLException {
        int result;
        try (Connection con = super.getConnection(); 
                PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO normal_partner.attendance "
                                + "(studentid, date_time) "
                                + "VALUES (?,?)")) {
            insert.setLong(1, studentid);
            Date now = new Date();
            insert.setTimestamp(2, new Timestamp(now.getTime()));
            result = insert.executeUpdate();
        }
        
        return result;
    }
    
    @Override
    public List<Attendance> findAll() throws SQLException {
        List<Attendance> all;
        try (Connection con = super.getConnection()) {
            all = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.attendance"); 
                    ResultSet rs = find.executeQuery()) {
                while (rs.next()) {
                    long studentid = rs.getLong("studentid");
                    Timestamp timestamp = rs.getTimestamp("date_time");
                    
                    Student student = sdao.findByID(studentid);
                    all.add(new Attendance(student, new DateTime(timestamp.getTime())));
                }
            }
        }
        
        return all;
    }

    @Override
    public List<Attendance> findByStudentId(long studentID) throws SQLException {
        List<Attendance> list;
        try (Connection con = super.getConnection()) {
            list = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.attendance a "
                         + "WHERE a.studentid=? "
                         + "ORDER BY a.date_time DESC")) {
                find.setLong(1, studentID);
                try (ResultSet rs = find.executeQuery()) {
                    Student student = sdao.findByID(studentID);
                    while (rs.next()) {
                        Timestamp timestamp = rs.getTimestamp("date_time");
                        list.add(new Attendance(student, new DateTime(timestamp.getTime())));
                    }
                }
            }
        }
        
        return list;
    }

    @Override
    public List<Attendance> findByDate(java.sql.Date date) throws SQLException {
        List<Attendance> list;
        try (Connection con = super.getConnection()) {
            list = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.attendance a "
                         + "WHERE cast(a.date_time as date) = ? "
                         + "ORDER BY a.date_time DESC")) {
                find.setDate(1, date);
                try (ResultSet rs = find.executeQuery()) {
                    while (rs.next()) {
                        long studentid = rs.getLong("studentid");
                        Timestamp timestamp = rs.getTimestamp("date_time");
                        
                        Student student = sdao.findByID(studentid);
                        list.add(new Attendance(student, new DateTime(timestamp.getTime())));
                    }
                }
            }
        }
        
        return list;
    }

    @Override
    public List<Attendance> findByMonth(long studentid, int month) throws SQLException {
        List<Attendance> list;
        try (Connection con = super.getConnection()) {
            list = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT * FROM normal_partner.attendance a "
                         + "WHERE MONTH(a.date_time) = ? "
                         + "AND a.studentid = ? "
                         + "ORDER BY a.date_time DESC")) {
                find.setInt(1, month);
                find.setLong(2, studentid);
                try (ResultSet rs = find.executeQuery()) {
                    Student student = sdao.findByID(studentid);
                    while (rs.next()) {
                        DateTime timestamp = new DateTime(rs.getTimestamp("date_time").getTime());
                        list.add(new Attendance(student, timestamp));
                    }
                }
            }
        }
        
        return list;
    }

    @Override
    public List<AttendanceReport> findReportByMonth(int month) throws SQLException {
        List<AttendanceReport> list;
        try (Connection con = super.getConnection()) {
            list = new ArrayList<>();
            try (PreparedStatement find = con.prepareStatement(
                         "SELECT studentid, count(*) as count FROM normal_partner.attendance "
                         + "WHERE MONTH(date_time) = ? GROUP BY studentid;")) {
                find.setInt(1, month);
                try (ResultSet rs = find.executeQuery()) {
                    while (rs.next()) {
                        long studentid = rs.getLong("studentid");
                        int count = rs.getInt("count");
                        
                        Student student = sdao.findByID(studentid);
                        list.add(new AttendanceReport(student, count));
                    }
                }
            }
        }
        
        return list;
    }

    @Override
    public int deleteAttendance(long studentid, long millis) throws SQLException {
        try (Connection con = super.getConnection();
                PreparedStatement del = con.prepareStatement(
                        "DELETE FROM normal_partner.attendance "
                        + "WHERE studentid = ? AND date_time = ?")) {
            del.setLong(1, studentid);
            del.setTimestamp(2, new java.sql.Timestamp(millis));
            System.out.println(studentid);
            System.out.println(new java.sql.Timestamp(millis));
            
            return del.executeUpdate();
        }
    }
}
