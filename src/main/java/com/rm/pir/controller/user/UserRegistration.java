package com.rm.pir.controller.user;

import com.rm.pir.controller.ActivationController;
import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.User;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;


@Named
@RequestScoped
public class UserRegistration {

    // injected objects
    @Inject
    private UserDAO userDAO;
    @Inject
    private ActivationController ac;
    
    //items to be used within the registration page
    private User newUser;
    
    @PostConstruct
    public void create() {
        newUser = new User();
    }
    
    public String register() {
        //add all elements to this instance
        //put the user in the database
        try {
            //set the created property of the new user
            java.util.Date now = new java.util.Date();
            newUser.setCreated(new java.sql.Date(now.getTime()));
            // user BCrypt to hash the password with salt
            newUser.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt()));
            newUser.setActivated(false);
            // get a random uid for activation
            String uuid = UUID.randomUUID().toString();
            newUser.setActivationKey(uuid);
            
            int count = userDAO.insert(newUser);
            // send activation email
            if (count == 1) {
                ac.setEmail(newUser.getEmail());
                return ac.sendActivationKey();
            } 
        } catch (SQLException ex) {
            Logger.getLogger(UserRegistration.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }    
}
