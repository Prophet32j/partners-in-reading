package com.rm.pir.controller.admin;

import com.rm.pir.controller.PasswordController;
import com.rm.pir.dao.interfaces.ChildAvailDAO;
import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.StudentAvailDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Student;
import com.rm.pir.model.User;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

@Named
@ViewAccessScoped
public class AdminEdit implements Serializable {
    
    @Inject
    private StudentDAO sdao;
    @Inject
    private ChildDAO cdao;
    @Inject
    private UserDAO udao;
    @Inject
    private StudentAvailDAO sadao;
    @Inject
    private ChildAvailDAO cadao;
    @Inject
    private PasswordController pc;
    
    // editing beans for user information
    private Student student;
    private Child child;
    private String day;
    private List<String> chosenHours;
    private Map<String, List<String>> daymap;
    private User user;
    
    /**
     * initializer of class
     */
    @PostConstruct
    public void init() {
        student = null;
        child = null;
        chosenHours = new ArrayList<>();
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(AdminEdit.class.getName()).log(Level.INFO, "AdminEdit destroyed");
//    }
    
    public void deleteUser(String email) {
        try {
            User user = new User();
            user.setEmail(email);
            udao.delete(user);
        } catch (SQLException ex) {
            Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * retrieves student details and availability and sets values to this student.
     * Returns the page to edit student information.
     * @param studentid
     * @return 
     */
    public String editStudent(String email) {
        try {
            student = sdao.findByEmail(email);
            daymap = sadao.findByID(student.getStudentID());
            if (daymap == null)
                daymap = new LinkedHashMap<>();
            chosenHours.clear();
        } catch (SQLException ex) {
            Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "edit-student";
    }
    
    /**
     * action method to save student details to the database
     */
    public String updateStudent() {
        if (student != null)
            try {
                student.setHomephone(com.rm.pir.utilities.Util.convertPhone(student.getHomephone()));
                student.setCellphone(com.rm.pir.utilities.Util.convertPhone(student.getCellphone()));
                sdao.update(student);
                return "search?faces-redirect=true";
            } catch (SQLException ex) {
                Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        return null;
    }
    
    public void deleteStudent(String email) {
        try {
//            User user = udao.findByEmail(email);
            User user = new User();
            user.setEmail(email);
            udao.delete(user);
        } catch (Exception ex) {
            Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * action method to save student availability to database
     */
    public void updateStudentAvailability() {
        if (student != null && daymap != null)
            try {
                student.setDaymap(daymap);
                sadao.update(student);
        } catch (SQLException ex) {
            Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * retrieves child information and availability and sets it to this child.
     * Returns the page to edit child information
     * @param childid
     * @return 
     */
    public String editChild(long childid) {
        try {
            child = cdao.findByID(childid);
            daymap = cadao.findByID(childid);
            if (daymap == null)
                daymap = new LinkedHashMap<>();
            chosenHours.clear();
        } catch (SQLException ex) {
            Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "edit-child";
    }
    
    public void deleteChild(long childid) {
        try {
            child = new Child();
            child.setChildID(childid);
            cdao.delete(child);
        } catch (Exception ex) {
            Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * action method to save child details to database
     */
    public void updateChild() {
        if (child != null)
            try {
                child.setHomephone(com.rm.pir.utilities.Util.convertPhone(child.getHomephone()));
                child.setCellphone(com.rm.pir.utilities.Util.convertPhone(child.getCellphone()));
                cdao.update(child);
            } catch (SQLException ex) {
                Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
    }
    
    /**
     * action method to save child availability to database
     */
    public void updateChildAvailability() {
        if (child != null && daymap != null)
            try {
                child.setDaymap(daymap);
                cadao.update(child);
        } catch (SQLException ex) {
            Logger.getLogger(AdminEdit.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    public void changeDay() {
        //clear chosenHours first
        chosenHours.clear();
        //now loop through dhlist and determine what DayHours match this day
        if (daymap.containsKey(day)) {
            List<String>maphours = daymap.get(day);
            for (int i=0; i<maphours.size(); i++) {
                chosenHours.add(maphours.get(i));
            }
        }
    }
    
    /*
     * this method will add all of the selected hours for the selected day
     * to the dhlist with new DayHour objects
     */
    public void addDay() {
        daymap.remove(day);
        List<String> maphours = new ArrayList<>();
        for(int i=0; i<chosenHours.size(); i++) {
            maphours.add(chosenHours.get(i));
        }
        daymap.put(day, maphours);
    }
    
    public void removeDay() {
        daymap.remove(day);
    }
    
    public void resetPassword(String email) {
        pc.setEmail(email);
        pc.invalidatePassword();
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
     * @return the child
     */
    public Child getChild() {
        return child;
    }

    /**
     * @param child the child to set
     */
    public void setChild(Child child) {
        this.child = child;
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
     * @return the chosenHours
     */
    public List<String> getChosenHours() {
        return chosenHours;
    }

    /**
     * @param chosenHours the chosenHours to set
     */
    public void setChosenHours(List<String> chosenHours) {
        this.chosenHours = chosenHours;
    }

    /**
     * @return the daymap
     */
    public Map<String, List<String>> getDaymap() {
        return daymap;
    }

    /**
     * @param daymap the daymap to set
     */
    public void setDaymap(Map<String, List<String>> daymap) {
        this.daymap = daymap;
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
}
