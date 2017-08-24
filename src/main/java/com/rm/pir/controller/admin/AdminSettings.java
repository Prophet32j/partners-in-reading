package com.rm.pir.controller.admin;

import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.Constants;
import com.rm.pir.model.Settings;
import com.rm.pir.model.User;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mindrot.jbcrypt.BCrypt;

@Named
@ViewAccessScoped
public class AdminSettings implements Serializable {
    
    private Settings settings;
    private Date date;
    private String password;
    private String account;
    private String message;
    private boolean changed;
    private int dateSelecter;
    
    @Inject
    private UserDAO dao;
    @Inject
    private Constants constants;
    
    @PostConstruct
    public void init() {
        date = null;
        setSettings(constants.getSettings());
    }
    
    public void changeDateSelected() {
        switch (dateSelecter) {
            case 1:
                date = settings.getSuspenseDate().toDate();
                break;
            case 2: 
                date = settings.getStartDate().toDate();
                 break;
            case 3:
                date = settings.getEndDate().toDate();
                break;
        }
    }
    
    public void changePassword() {
        User user = new User();
        if (account.equals("Admin"))
            user.setEmail("admin");
        else
            user.setEmail("frontdesk");
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        try {
            dao.changePassword(user);
            message = account + " password changed successfully!";
            changed = true;
            Logger.getLogger(AdminSettings.class.getName()).log(Level.INFO, "*** {0} password has been changed! ***", account);
        } catch (SQLException ex) {
            Logger.getLogger(AdminSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void submit() {
        changed = false;
        switch (dateSelecter) {
            case 1:
                settings.setSuspenseDate(new DateTime(date.getTime()));
                break;
            case 2:
                settings.setStartDate(new DateTime(date.getTime()));
                break;
            case 3:
                settings.setEndDate(new DateTime(date.getTime()));
                break;
        }
        constants.setSettings(settings);
    }
    
    public void toggleRegistration() {
        changed = false;
        settings.setRegistrationOpen(!settings.isRegistrationOpen());
        constants.setSettings(settings);
    }
    
    public String formatDate() {
        if (date == null)
            return null;
        DateTime dt = new DateTime(date.getTime());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, MMM d, yyyy");
        return formatter.print(dt);
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
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the user
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the user to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * @return the dateSelecter
     */
    public int getDateSelecter() {
        return dateSelecter;
    }

    /**
     * @param dateSelecter the dateSelecter to set
     */
    public void setDateSelecter(int dateSelecter) {
        this.dateSelecter = dateSelecter;
    }
}
