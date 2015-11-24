package com.rm.pir.controller;

import com.rm.pir.utilities.MailSender;
import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.User;
import com.rm.pir.utilities.Constants;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.mindrot.jbcrypt.BCrypt;

@Named
@ViewAccessScoped
public class PasswordController implements Serializable{
    
    @Inject 
    private UserDAO dao;

    private String email;
    private String password;
    private String key;
    private boolean verified;
    
    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        email = params.get("email");
        key = params.get("key");
        verified = verifyUser();
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(PasswordController.class.getName()).log(Level.INFO, "PasswordController destoyed");
//    }
    
    public String resetPassword() {
        try {
            User user = dao.findByEmail(email);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            dao.changePassword(user);
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "password-updated?faces-redirect=true";
        } catch (SQLException ex) {
            Logger.getLogger(PasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String invalidatePassword() {
        try {
            User user = dao.findByEmail(email);
            if (user != null) {
                // generate random password
                String uid = UUID.randomUUID().toString();
                user.setPassword(BCrypt.hashpw(uid, BCrypt.gensalt()));
                dao.changePassword(user);
                sendResetEmail(uid, email);
                
                return "reset-sent";
            }
        } catch (SQLException ex) {
            Logger.getLogger(PasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean verifyUser() {
        if (email != null && key != null)
            try {
                User user = dao.findByEmail(email);
                return user != null && BCrypt.checkpw(key, user.getPassword());
            } catch (SQLException ex) {
                Logger.getLogger(PasswordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        return false;
    }
        
    public void sendResetEmail(String uid, String email) {
        String link = Constants.SERVER_URL + "reset-password.xhtml?"
                + "key=" + uid + "&email=" + email;
        String subject="Partners In Reading - Password Reset";      
        String body = "Hello, Partner! <br> "
                + "This email has been generated because you have reset your password. <br> "
                + "To reset your password, please click on the following link: <br><br>"
                + "<a href='" + link + "'>" + link + "</a> <br><br> "
                + "If you received this email in error and did not reset your password, please call "
                + "the library and ask for a Partners In Reading staff member."
                + "Thanks!<br><br>"
                + "Partners In Reading Staff <br>"
                + "Normal Public Library";
                
        new MailSender(email,subject,body).send();
    }
        
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the verified
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * @param verified the verified to set
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
