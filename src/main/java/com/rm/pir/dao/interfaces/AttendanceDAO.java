package com.rm.pir.dao.interfaces;

import com.rm.pir.model.Attendance;
import com.rm.pir.model.AttendanceReport;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface AttendanceDAO {
    
    public int takeAttendance(long studentid) throws SQLException;
    public List<Attendance> findAll() throws SQLException;
    public List<Attendance> findByStudentId(long studentID) throws SQLException;
    public List<Attendance> findByDate(Date date) throws SQLException;
    public List<Attendance> findByMonth(long studentid, int month) throws SQLException;
    public List<AttendanceReport> findReportByMonth(int month) throws SQLException;
    public int deleteAttendance(long studentid, long millis) throws SQLException;
}
