package com.rm.pir.controller.child;

import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.ChildPartnerDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Constants;
import com.rm.pir.model.Family;
import com.rm.pir.model.Partner;
import com.rm.pir.model.Settings;
import com.rm.pir.model.User;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

@Named
@ViewAccessScoped
public class FamilyController implements Serializable {
    
    @Inject 
    private ChildDAO cdao;
    @Inject 
    private Family family;
    @Inject 
    private User user;
    @Inject
    private ChildPartnerDAO cpdao;
    @Inject
    private Constants constants;
    
    private Child newChild;
    private Partner partner;
    private boolean update;
    private boolean add;
    private boolean requesting;
    private Settings settings;
    
    @PostConstruct
    public void init() {
        cancel();
        setSettings(constants.getSettings());
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(FamilyController.class.getName()).log(Level.INFO, "ChildPartner destroyed");
//    }
    
    public void requestPartner(long childid) {
        try {
            requesting = true;
            update = false;
            add = false;
            partner = cpdao.findByID(childid);
            if (partner == null) {
                partner = new Partner();
                partner.setID(childid);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FamilyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteRequest(long childid) {
        try {
            cancel();
            cpdao.delete(childid);
        } catch (SQLException ex) {
            Logger.getLogger(FamilyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updatePartner() {
        try {
            cpdao.update(partner);
            requesting = false;
        } catch (SQLException ex) {
            Logger.getLogger(FamilyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void remove(Child child) {
        try {
            cdao.delete(child);
            family.getFamlist().remove(child);
        } catch (SQLException ex) {
            Logger.getLogger(FamilyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void create() {
        if (!settings.isRegistrationOpen())
            return;
        newChild = new Child();
        if (!family.getFamlist().isEmpty()) {
            Child child = family.getFamlist().get(0);
            newChild.setLastname(child.getLastname());
            newChild.setHomephone(child.getHomephone());
            newChild.setCellphone(child.getCellphone());
            newChild.setParent_one(child.getParent_one());
        }
        add = true;
        update = false;
        requesting = false;
    }
    
    public void editChild(long childid) {
        try {
            newChild = cdao.findByID(childid);
            update = true;
            add = false;
            requesting = false;
        } catch (SQLException ex) {
            Logger.getLogger(FamilyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateChild() {
        try {
            cancel();
            newChild.setHomephone(com.rm.pir.utilities.Util.convertPhone(newChild.getHomephone()));
            newChild.setCellphone(com.rm.pir.utilities.Util.convertPhone(newChild.getCellphone()));
            int result = cdao.update(newChild);
            if (result == 1) {
                List<Child> list = family.getFamlist();
                for (int i=0; i<list.size();i++) {
                    if (list.get(i).getChildID() == newChild.getChildID()) {
                        list.set(i, newChild);
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FamilyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String addChild() {
        if (!settings.isRegistrationOpen())
            return null;
        newChild.setHomephone(com.rm.pir.utilities.Util.convertPhone(newChild.getHomephone()));
        newChild.setCellphone(com.rm.pir.utilities.Util.convertPhone(newChild.getCellphone()));
        newChild.setEmail(user.getEmail());
        try {
            long key = cdao.insert(newChild);
            if (key > 0) {
                newChild.setChildID(key);
                family.getFamlist().add(newChild);
                return "edit-times?faces-redirect=true";
            }
        } catch (SQLException ex) {
            Logger.getLogger(FamilyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void cancel() {
        requesting = false;
        update = false;
        add = false;
    }

    public Child getNewChild() {
        return newChild;
    }

    public void setNewChild(Child newChild) {
        this.newChild = newChild;
    }

    /**
     * @return the update
     */
    public boolean isUpdate() {
        return update;
    }

    /**
     * @param update the update to set
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }

    /**
     * @return the add
     */
    public boolean isAdd() {
        return add;
    }

    /**
     * @param add the add to set
     */
    public void setAdd(boolean add) {
        this.add = add;
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
     * @return the requesting
     */
    public boolean isRequesting() {
        return requesting;
    }

    /**
     * @param requesting the requesting to set
     */
    public void setRequesting(boolean requesting) {
        this.requesting = requesting;
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
