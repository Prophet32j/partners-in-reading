package com.rm.pir.model;

import com.rm.pir.dao.interfaces.SettingsDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.joda.time.DateTime;

@Named
@ApplicationScoped
public class Constants implements Serializable {
    
    @Inject
    private SettingsDAO dao;
    
    private Settings settings;
    
    private SelectItem[] hours;
    private SelectItem[] days;
    
    @PostConstruct
    public void init() {
        hours = new SelectItem[] {
            new SelectItem("3:00pm"),
            new SelectItem("3:30pm"),
            new SelectItem("4:00pm"),
            new SelectItem("4:30pm"),
            new SelectItem("5:00pm"),
            new SelectItem("5:30pm"),
            new SelectItem("6:00pm"),
            new SelectItem("6:30pm"),
            new SelectItem("7:00pm"),
            new SelectItem("7:30pm"),
            new SelectItem("8:00pm")
        };
        days = new SelectItem[] {
            new SelectItem("Monday"),
            new SelectItem("Tuesday"),
            new SelectItem("Wednesday"),
            new SelectItem("Thursday")
        };
        try {
            settings = dao.getSettings();
            if (settings == null) 
                setSettings(new Settings());
        } catch (SQLException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PreDestroy
    public void destroy() {
        Logger.getLogger(Constants.class.getName()).log(Level.INFO, "Constants destroyed");
    }

    /**
     * @return the hours
     */
    public SelectItem[] getHours() {
        return hours;
    }

    /**
     * @return the days
     */
    public SelectItem[] getDays() {
        return days;
    }
    
    public List<SelectItem> getGrades() {
        List<SelectItem> grades = new ArrayList<>();
        // determine if we are in the spring or fall semester
        DateTime now = new DateTime();
        if (now.getMonthOfYear() < 4)
            grades.add(new SelectItem("k"));
        for (int i=1; i<7; i++) {
            grades.add(new SelectItem(String.valueOf(i)));
        }
        return grades;
    }
    
    public List<SelectItem> getAges() {
        List<SelectItem> ages = new ArrayList<>();
        for (int i=5; i<13; i++)
            ages.add(new SelectItem(String.valueOf(i)));
        return ages;
    }

    /**
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
        try {
            dao.saveSettings(settings);
        } catch (SQLException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
