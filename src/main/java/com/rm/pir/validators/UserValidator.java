package com.rm.pir.validators;

import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.mindrot.jbcrypt.BCrypt;

@Named
@RequestScoped
public class UserValidator {
    
    @Inject
    private User user;
    @Inject
    private UserDAO dao;
    
    public void validateEmail(FacesContext fc, UIComponent ui, Object value) {
        String email = (String) value;
        try {
            if (dao.existsByEmail(email)) {
                throw new ValidatorException(
                    new FacesMessage("Email is already registered."));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void validatePass(FacesContext fc, UIComponent ui, Object value) {
        String pass = (String) value;
        if (!BCrypt.checkpw(pass, user.getPassword())) {
            throw new ValidatorException(
                    new FacesMessage("Incorrect current password"));
        }
    }
}
