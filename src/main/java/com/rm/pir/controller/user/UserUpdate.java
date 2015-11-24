package com.rm.pir.controller.user;

import com.rm.pir.controller.ActivationController;
import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

@Named
@RequestScoped
public class UserUpdate {

    private String newPass;
    private String currentPass;
    private String email;
    @Inject
    private User user;
    @Inject
    private UserDAO dao;
    @Inject
    private ActivationController ac;

    public void updatePassword() {
        user.setPassword(BCrypt.hashpw(newPass, BCrypt.gensalt()));
        try {
            dao.changePassword(user);
        } catch (SQLException ex) {
            Logger.getLogger(UserUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String updateEmail() {
        try {
            int result = dao.updateEmail(email, user);
            if (result == 1) {
                ac.setEmail(email);
                return ac.sendActivationKey();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
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
