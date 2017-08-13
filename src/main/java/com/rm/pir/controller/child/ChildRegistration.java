package com.rm.pir.controller.child;

import com.rm.pir.controller.student.StudentPartner;
import com.rm.pir.utilities.Mailer;
import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Family;
import com.rm.pir.model.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ChildRegistration {
    
    @Inject 
    private User user;
    @Inject
    private ChildDAO dao;
    @Inject 
    private Family family;
    
    private boolean hasReqPartner;
    private Child newChild;

    @PostConstruct
    public void init() {
        newChild = new Child();
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(ChildRegistration.class.getName()).log(Level.INFO, "ChildRegistration destroyed");
//    }
    
    public String register() {
        try {
            newChild.setHomephone(com.rm.pir.utilities.Util.convertPhone(newChild.getHomephone()));
            newChild.setCellphone(com.rm.pir.utilities.Util.convertPhone(newChild.getCellphone()));
            newChild.setEmail(user.getEmail());
            long key = dao.insert(newChild);
            if (key > 0) {
                newChild.setChildID(key);
                family.getFamlist().add(newChild);
                //email confirmation
                try {
                    sendConfirmation();
                } catch (Exception ex) {
                    Logger.getLogger(ChildRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
                return hasReqPartner ? "request-partner?faces-redirect=true" : 
                        "edit-times?faces-redirect=true";
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChildRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public Child getNewChild() {
        return newChild;
    }

    public void setNewChild(Child newChild) {
        this.newChild = newChild;
    }    
    
    public void sendConfirmation(){
        String to = newChild.getEmail();
        String subject="Welcome " + newChild.getFirstname() 
                + " " + newChild.getLastname() + ". Thank you for registering!";
        String body = 
            "<html>"
                + "<center><body>"
                   + "<h3>Thank you for registering!</h3>"
                   + "<p>If you haven't done so already, don't forget to submit your "
                   + "child's availability to us. You can do so through the Dashboard</p>"
                   + "<table>"
                       + "<tr>"
                           + "<td>Email: </td>"
                           + "<td>"+user.getEmail()+"</td>"
                       + "</tr>"
                       + "<tr>		"
                           + "<td>First Name: </td>"
                           + "<td>"+newChild.getFirstname()+"</td>"
                       + "</tr>"
                       + "<tr>		"
                           + "<td>Last Name: </td>"
                           + "<td>"+newChild.getLastname()+"</td>"
                       + "</tr>"
                       + "<tr>		"
                           + "<td>Gender: </td>"
                           + "<td>"+newChild.getGender()+"</td>"
                       + "</tr>"
                       + "<tr>		"
                           + "<td>Home Phone: </td>"
                           + "<td>"+newChild.getHomephone()+"</td>"
                       + "</tr>"
                       + "<tr>		"
                           + "<td>Cell Phone: </td>"
                           + "<td>"+newChild.getCellphone()+"</td>"
                       + "</tr>"
                       + "<tr>		"
                           + "<td>Special Needs: </td>"
                           + "<td>"+newChild.isSpecial_needs()+"</td>"
                       + "</tr>"
                       + "<tr>		"
                           + "<td>Language Needs: </td>"
                           + "<td>"+newChild.isLanguage_needs()+"</td>"
                       + "</tr>"
                   + "</table>"
               + "</body></center>"
            + "</html>";
                
        Mailer email = new Mailer(to,subject,body);
        email.send();
    }

    /**
     * @return the hasReqPartner
     */
    public boolean isHasReqPartner() {
        return hasReqPartner;
    }

    /**
     * @param hasReqPartner the hasReqPartner to set
     */
    public void setHasReqPartner(boolean hasReqPartner) {
        this.hasReqPartner = hasReqPartner;
    }
}
