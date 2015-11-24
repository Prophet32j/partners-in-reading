/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.controller.child;

import com.rm.pir.dao.interfaces.ChildAvailDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Family;
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
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

@Named
@ViewAccessScoped
public class ChildTimes implements Serializable {
    
    @Inject 
    private ChildAvailDAO dao;
//    @Inject private ChildDAO childdao;
//    @Inject private User user;
    @Inject 
    private Family family;
    
    private String day;
    private List<String> chosenHours;
    private Map<String, List<String>> daymap;
    private String notComplete;
    private List<SelectItem> children;
    private int selectedChild;
    
    @PostConstruct
    public void create() {
        selectedChild = -1;
        chosenHours = new ArrayList<>();
        children = new ArrayList<>();
        List<Child> temp = family.getFamlist();
        for (int i=0; i<temp.size(); i++) {
            children.add(new SelectItem(i, temp.get(i).getFirstname()));
        }
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(ChildTimes.class.getName()).log(Level.INFO, "ChildTimes destroyed");
//    }
    
    public void validateChosen(FacesContext fc, UIComponent ui, Object value) {
        if (daymap.isEmpty()) {
            FacesMessage message = new FacesMessage("Please submit at least one day for availability");
            throw new ValidatorException(message);
        }
    }
    
    public void changeChild() throws SQLException {
        daymap = dao.findByID(family.getFamlist().get(selectedChild).getChildID());
        if (daymap == null)
            daymap = new LinkedHashMap<>();
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
        if (day != null)
            daymap.remove(day);
    }
    
    public void finished() {
        if (!daymap.isEmpty()) {
//            notComplete = "You must enter at least one day of availability";
            
            //here is where we will write to the database
            try {
                family.getFamlist().get(selectedChild).setDaymap(daymap);

                int result = dao.update(family.getFamlist().get(selectedChild));
                if (result == 1) {
                    //reset form and return
                    reset();
                }
            } catch (SQLException e) {
                notComplete = "Something went wrong in method finished()";
                System.out.println(e.getMessage());
            }
        }
    }
    
    private void reset() {
        chosenHours.clear();
        selectedChild = -1;
        daymap = null;
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

    public List<SelectItem> getChildren() {
        return children;
    }

    public void setChildren(List<SelectItem> children) {
        this.children = children;
    }

    public int getSelectedChild() {
        return selectedChild;
    }

    public void setSelectedChild(int selectedChild) {
        this.selectedChild = selectedChild;
    }
}
