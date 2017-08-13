package com.rm.pir.controller;

import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.User;
import com.rm.pir.utilities.Mailer;
import com.rm.pir.utilities.Constants;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ActivationController {
    
    private String email;
    private String key;
    private boolean activated;
    
    @Inject
    private UserDAO dao;
    
    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        key = params.get("key");
        activated = checkAndActivate(key);
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(ActivationController.class.getName()).log(Level.INFO, "ActivationController destoyed");
//    }
    
    public String sendActivationKey() {
        try {
            User user = dao.findByEmail(email);
            user.setActivated(false);
            // get new activation key
            String uuid = UUID.randomUUID().toString();
            user.setActivationKey(uuid);
            
            // deactivate user in database and send email
            dao.deactivate(user);
            sendActivationEmail(user);
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

            return "activation-sent?faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(ActivationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String sendActivationKey(User user) {
        try {
            sendActivationEmail(user);        
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

            return "activation-sent?faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(ActivationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private boolean checkAndActivate(String key) {
        if (key != null && !key.equals(""))
            try {
                User user = dao.findByActivationKey(key);
                // if returned user is null, return false, otherwise activate
                if (user == null)
                    return false;
                user.setActivated(true);
                dao.activate(user);

                // user is now activated
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(ActivationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        return false;
    }
    
    private void sendActivationEmail(User user) {
        String link = Constants.SERVER_URL + "activate.xhtml?key=" + user.getActivationKey();
        String to = user.getEmail();
        String subject = "Partners In Reading - Account Activation";
        String body = "Hello, Partner! <br> "
                + "Thank you for registering for Partners In Reading. <br> "
                + "To activate your account, please click on the following link: <br><br>"
                + "<a href='" + link + "'>" + link + "</a> <br> "
                + "Thanks!<br><br>"
                + "Partners In Reading Staff <br>"
                + "Normal Public Library";
        
        new Mailer(to,subject,body).send();
    }

    /**
     * @return the activated
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * @param activated the activated to set
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
