/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.validators;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

@Named
@RequestScoped
public class ChildValidator {
    
    public void validatePhone(FacesContext fc, UIComponent ui, Object value) {
        FacesMessage message = new FacesMessage("Phone number must be 10 digits long");
        String phone = (String) value;
        phone = phone.replaceAll("\\D", "");
        if (phone.length() != 10)
            throw new ValidatorException(message);
    }
}
