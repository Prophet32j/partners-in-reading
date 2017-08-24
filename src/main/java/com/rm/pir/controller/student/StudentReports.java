package com.rm.pir.controller.student;

import com.rm.pir.dao.interfaces.AttendanceDAO;
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
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

@Named
@ViewAccessScoped
public class StudentReports implements Serializable {
    
    private int month;
    private List<Attendance> attendances;
    
//    @Inject
//    private User user;
    @Inject
    private Student student;
    @Inject
    private AttendanceDAO adao;
    
    @PostConstruct
    public void init() {
        month = -1;
        attendances = new ArrayList<>();
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(StudentReports.class.getName()).log(Level.INFO, "StudentReports destroyed");
//    }
    
    /**
     * find all attendances recorded for month for the student
     */
    public void findByMonth() {
        try {
            attendances = adao.findByMonth(student.getStudentID(), month);
        } catch (SQLException ex) {
            Logger.getLogger(StudentReports.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * clears attendances list
     */
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
