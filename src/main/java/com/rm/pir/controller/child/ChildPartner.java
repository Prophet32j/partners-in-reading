package com.rm.pir.controller.child;

import com.rm.pir.controller.student.StudentPartner;
import com.rm.pir.dao.interfaces.ChildPartnerDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Family;
import com.rm.pir.model.Partner;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

@ViewAccessScoped
@Named
public class ChildPartner implements Serializable {
    
    @Inject
    private Family family;
    @Inject
    private ChildPartnerDAO cpdao;
    
    private long childid;
    private Partner partner;
    
    @PostConstruct
    public void init() {
        partner = new Partner();
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(ChildPartner.class.getName()).log(Level.INFO, "ChildPartner destroyed");
//    }
    
    public String register() {
        addRequest();
        return "edit-times?faces-redirect=true";
    }
    
    private void addRequest() {
        try {
            partner.setID(childid);
            cpdao.insert(partner);
        } catch (SQLException ex) {
            Logger.getLogger(StudentPartner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the family
     */
    public Family getFamily() {
        return family;
    }

    /**
     * @param family the family to set
     */
    public void setFamily(Family family) {
        this.family = family;
    }

    /**
     * @return the childid
     */
    public long getChildid() {
        return childid;
    }

    /**
     * @param childid the childid to set
     */
    public void setChildid(long childid) {
        this.childid = childid;
    }
    
    public List<SelectItem> getChildren() {
        List<SelectItem> list = new ArrayList<>();
        for (Child child : family.getFamlist()) {
            list.add(new SelectItem(child.getChildID(), child.getFirstname()));
        }
        return list;
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
