package com.rm.pir.controller.admin;

import com.rm.pir.dao.interfaces.AttendanceDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Attendance;
import com.rm.pir.model.Student;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

@Named
@ViewAccessScoped
public class AdminAttendance implements Serializable {
    
    private int month;
    private long studentid;
    private List<Student> students;
    private List<Attendance> attendances;
        
    @Inject
    private AttendanceDAO adao;
    @Inject
    private StudentDAO sdao;
    
    @PostConstruct
    public void init() {
        month = -1;
        attendances = new ArrayList<>();
        try {
            students = sdao.findAll();
        } catch (SQLException ex) {
            Logger.getLogger(AdminAttendance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void search() {
        try {
            attendances = adao.findByMonth(studentid, month);
        } catch (SQLException ex) {
            Logger.getLogger(AdminAttendance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteAttendance(long millis) {
        try {
            adao.deleteAttendance(studentid, millis);
            search();
        } catch (SQLException ex) {
            Logger.getLogger(AdminAttendance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reset() {
        month = -1;
        attendances.clear();
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the studentid
     */
    public long getStudentid() {
        return studentid;
    }

    /**
     * @param studentid the studentid to set
     */
    public void setStudentid(long studentid) {
        this.studentid = studentid;
    }

    /**
     * @return the students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * @return the attendances
     */
    public List<Attendance> getAttendances() {
        return attendances;
    }

    /**
     * @param attendances the attendances to set
     */
    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }
}
