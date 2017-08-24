/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.controller.admin;

import com.rm.pir.dao.interfaces.PairingDAO;
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
public class AdminSchedule implements Serializable {
    
    @Inject 
    private PairingDAO pdao;
//    @Inject
//    private StudentDAO sdao;
//    @Inject
//    private ChildDAO cdao;
    
    private List<Session> seslist;
        
    @PostConstruct
    private void create() {
        try {
            seslist = pdao.findAll();
        } catch (SQLException ex) {
            Logger.getLogger(AdminSchedule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(AdminSchedule.class.getName()).log(Level.INFO, "AdminSchedule destroyed");
//    }
    
    public void delete(Session session) throws SQLException {
        pdao.delete(session);
        seslist.remove(session);
    }

    public List<Session> getSeslist() {
        return seslist;
    }

    public void setSeslist(List<Session> seslist) {
        this.seslist = seslist;
    }
}
