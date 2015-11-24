package com.rm.pir.model;

import javax.inject.Named;
import org.joda.time.DateTime;

@Named
public class Settings {
    
    private boolean registrationOpen;
    private DateTime suspenseDate;
    private DateTime startDate;
    private DateTime endDate;

    public Settings() {
        this(true, new DateTime(), new DateTime(), new DateTime());
    }

    public Settings(boolean registrationOpen, DateTime suspenseDate, 
            DateTime startDate, DateTime endDate) {
        this.registrationOpen = registrationOpen;
        this.suspenseDate = suspenseDate;
        this.startDate = startDate;
        this.endDate = endDate;
        
    }
    /**
     * @return the registrationOpen
     */
    public boolean isRegistrationOpen() {
        return registrationOpen;
    }

    /**
     * @param registrationOpen the registrationOpen to set
     */
    public void setRegistrationOpen(boolean registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    /**
     * @return the suspenseDate
     */
    public DateTime getSuspenseDate() {
        return suspenseDate;
    }

    /**
     * @param suspenseDate the suspenseDate to set
     */
    public void setSuspenseDate(DateTime suspenseDate) {
        this.suspenseDate = suspenseDate;
    }

    /**
     * @return the startDate
     */
    public DateTime getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public DateTime getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }
}
