package com.rm.pir.controller.student;

import com.rm.pir.dao.interfaces.StudentAvailDAO;
import com.rm.pir.model.Student;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

@Named
@ViewAccessScoped
public class StudentTimes implements Serializable {
    
    @Inject 
    private StudentAvailDAO dao;
    @Inject 
    private Student student;
    
    private String day;
    private List<String> chosenHours;
    private Map<String, List<String>> daymap;
    private String notComplete;
    
    @PostConstruct
    public void create() {
        chosenHours = new ArrayList<>();
        reset();
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(StudentTimes.class.getName()).log(Level.INFO, "StudentTimes destroyed");
//    }
    
    public void validateChosen(FacesContext fc, UIComponent ui, Object value) {
        if (daymap.isEmpty()) {
            FacesMessage message = new FacesMessage("Please submit at least one day to availability");
            throw new ValidatorException(message);
        }
    }
    
    public void reset() {
        try {
            daymap = dao.findByID(student.getStudentID());
            if (daymap == null)
                daymap = new LinkedHashMap<>();
            chosenHours.clear();
            day = "";
        } catch (SQLException ex) {
            Logger.getLogger(StudentTimes.class.getName()).log(Level.SEVERE, null, ex);
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
        if (day != null && chosenHours.size() > 0) {
            daymap.remove(day);
            List<String> maphours = new ArrayList<>();
            for(int i=0; i<chosenHours.size(); i++) {
                maphours.add(chosenHours.get(i));
            }
            daymap.put(day, maphours);
        }
        
    }
    
    public void removeDay() {
        daymap.remove(day);
    }
    
    public String finished() {
        if (!daymap.isEmpty()) {
            try {
                student.setDaymap(daymap);
                int result = dao.update(student);
                if (result == 1) 
                    return "dashboard?faces-redirect=true";
            } catch (SQLException ex) {
                Logger.getLogger(StudentTimes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public String getNotComplete() {
        return notComplete;
    }

    public void setNotComplete(String notComplete) {
        this.notComplete = notComplete;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List getChosenHours() {
        return chosenHours;
    }

    public void setChosenHours(List chosenHours) {
        this.chosenHours = chosenHours;
    }

    public Map<String, List<String>> getDaymap() {
        return daymap;
    }

    public void setDaymap(Map<String, List<String>> daymap) {
        this.daymap = daymap;
    }
}
