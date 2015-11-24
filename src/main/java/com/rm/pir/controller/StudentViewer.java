package com.rm.pir.controller;

import com.rm.pir.controller.admin.AdminQuery;
import com.rm.pir.dao.interfaces.PairingDAO;
import com.rm.pir.dao.interfaces.StudentAvailDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.dao.interfaces.StudentPartnerDAO;
import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.Partner;
import com.rm.pir.model.Session;
import com.rm.pir.model.Student;
import com.rm.pir.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class StudentViewer {
    
    private User user;
    private Student student;
    private List<Session> sessions;
    private String day;
    private String hour;
    private List<Partner> partners;
    
    @Inject
    private StudentAvailDAO sadao;
    @Inject
    private UserDAO userdao;
    @Inject
    private StudentDAO studentdao;
    @Inject
    private StudentPartnerDAO spdao;
    @Inject
    private PairingDAO pdao;
    

//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(StudentViewer.class.getName()).log(Level.INFO, "StudentViewer destoyed");
//    }
    
    public String view(String email) {
        try {
            user = userdao.findByEmail(email);
            student = studentdao.findByEmail(email);
            Map<String, List<String>> map = sadao.findByID(student.getStudentID());
            student.setDaymap(map);
            sessions = pdao.findByStudent(student);
            partners = spdao.findByID(student.getStudentID());
            return "student-details";
        } catch (SQLException ex) {
            Logger.getLogger(AdminQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @return the sessions
     */
    public List<Session> getSessions() {
        return sessions;
    }

    /**
     * @param sessions the sessions to set
     */
    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    /**
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * @return the hour
     */
    public String getHour() {
        return hour;
    }

    /**
     * @param hour the hour to set
     */
    public void setHour(String hour) {
        this.hour = hour;
    }

    /**
     * @return the partners
     */
    public List<Partner> getPartners() {
        return partners;
    }

    /**
     * @param partners the partners to set
     */
    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }
}
