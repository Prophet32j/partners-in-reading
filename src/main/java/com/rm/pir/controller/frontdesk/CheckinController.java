package com.rm.pir.controller.frontdesk;

import com.rm.pir.dao.interfaces.AttendanceDAO;
import com.rm.pir.dao.interfaces.PairingDAO;
import com.rm.pir.dao.interfaces.StudentPartnerDAO;
import com.rm.pir.model.Partner;
import com.rm.pir.model.Session;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

@Named
@ViewAccessScoped
public class CheckinController implements Serializable {
    
    private List<Session> sessions;
    private List<Partner> requests;
    private String searchString;
    
    @Inject
    private PairingDAO pdao;
    @Inject
    private AttendanceDAO adao;
    @Inject
    private StudentPartnerDAO spdao;
    
    @PostConstruct
    public void init() {
        try {
            // get all paired reading sessions
            setSessions(pdao.findAll());
            requests = spdao.findAll();
        } catch (SQLException ex) {
            Logger.getLogger(CheckinController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(CheckinController.class.getName()).log(Level.INFO, "CheckinController destroyed");
//    }
    
    public void find() {
        try {
            setSessions(pdao.search(searchString));
        } catch (SQLException ex) {
            Logger.getLogger(CheckinController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    public void reset() {
        try {
            searchString = "";
            setSessions(pdao.findAll());
        } catch (SQLException ex) {
            Logger.getLogger(CheckinController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * take attendance / check in a paired reading session
     */
    public String checkin(long studentid) {
        try {
            adao.takeAttendance(studentid);
        } catch (SQLException ex) {
            Logger.getLogger(CheckinController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "dashboard?faces-redirect=true";
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
     * @return the searchString
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * @param searchString the searchString to set
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * @return the requests
     */
    public List<Partner> getRequests() {
        return requests;
    }

    /**
     * @param requests the requests to set
     */
    public void setRequests(List<Partner> requests) {
        this.requests = requests;
    }
}
