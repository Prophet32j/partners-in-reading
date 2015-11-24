package com.rm.pir.controller;

import com.rm.pir.dao.interfaces.ChildAvailDAO;
import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.ChildPartnerDAO;
import com.rm.pir.dao.interfaces.PairingDAO;
import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Partner;
import com.rm.pir.model.Session;
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
public class ChildViewer {
    
    private User user;
    private Child child;
    private Session partnerSession;
    private Partner partner;
    
    @Inject
    private UserDAO userdao;
    @Inject
    private ChildDAO childdao;
    @Inject
    private ChildAvailDAO cadao;
    @Inject
    private PairingDAO pdao;
    @Inject
    private ChildPartnerDAO cpdao;
    
    
//    @PostConstruct
//    public void init() {
//    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(ChildViewer.class.getName()).log(Level.INFO, "ChildViewer destroyed");
//    }
    
    public String view(long childID) {
        try {
            
            child = childdao.findByID(childID);
            user = userdao.findByEmail(child.getEmail());
            Map<String, List<String>> map = cadao.findByID(child.getChildID());
            child.setDaymap(map);
            partnerSession = pdao.findByChild(child);
            partner = cpdao.findByID(childID);
            return "child-details";
        } catch (SQLException ex) {
            Logger.getLogger(ChildViewer.class.getName()).log(Level.SEVERE, null, ex);
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
     * @return the partnerSession
     */
    public Session getPartnerSession() {
        return partnerSession;
    }

    /**
     * @param partnerSession the partnerSession to set
     */
    public void setPartnerSession(Session partnerSession) {
        this.partnerSession = partnerSession;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
