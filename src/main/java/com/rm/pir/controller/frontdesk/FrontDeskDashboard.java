package com.rm.pir.controller.frontdesk;

import com.rm.pir.controller.admin.AdminQuery;
import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.AttendanceDAO;
import com.rm.pir.dao.interfaces.PairingDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Attendance;
import com.rm.pir.model.Session;
import com.rm.pir.utilities.Dates;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.joda.time.DateTime;

@Named
@ViewAccessScoped
public class FrontDeskDashboard implements Serializable {
    
    private List<Session> sessions;
    private List<Attendance> checkedIn;
    
    @Inject 
    private ChildDAO childdao;
    @Inject
    private StudentDAO studentdao;
    @Inject
    private AttendanceDAO attenddao;
    @Inject
    private PairingDAO pairdao;
    
    @PostConstruct
    public void init() {
        sessions = new ArrayList<>();
        checkedIn = new ArrayList<>();
        try {
            getTodaysSessions();
            getCheckedInSessions();
        } catch (SQLException ex) {
            Logger.getLogger(AdminQuery.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void deleteAttendance(long studentid, long millis) {
        try {
            attenddao.deleteAttendance(studentid, millis);
            getCheckedInSessions();
        } catch (SQLException ex) {
            Logger.getLogger(FrontDeskDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * retrieves all reading sessions for this day
     * @throws SQLException 
     */
    private void getTodaysSessions() throws SQLException {
        DateTime today = new DateTime();
        sessions = pairdao.findByDay(Dates.getDayOfWeek(today.getDayOfWeek()));
    }
    
    /**
     * retrieves all checked in sessions for this day
     */
    public void getCheckedInSessions() {
        // get today's date in SQL
        Date today = new Date();
        try {
            checkedIn = attenddao.findByDate(new java.sql.Date(today.getTime()));
        } catch (SQLException ex) {
            Logger.getLogger(FrontDeskDashboard.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    public void checkIn(Session session) throws SQLException {
        attenddao.takeAttendance(session.getStudent().getStudentID());
        // refresh the checked in sessions
        getCheckedInSessions();
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    /**
     * @return the checkedIn
     */
    public List<Attendance> getCheckedIn() {
        return checkedIn;
    }

    /**
     * @param checkedIn the checkedIn to set
     */
    public void setCheckedIn(List<Attendance> checkedIn) {
        this.checkedIn = checkedIn;
    }
}
