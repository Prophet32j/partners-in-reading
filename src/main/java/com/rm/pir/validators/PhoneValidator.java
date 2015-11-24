package com.rm.pir.validators;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

@Named
@RequestScoped
public class PhoneValidator {
    
    public void validatePhone(FacesContext fc, UIComponent ui, Object value) {
        FacesMessage message = new FacesMessage("Phone numbers must include area code");
        String phone = (String) value;
        phone = phone.replaceAll("\\D", "");
        if (phone.length() != 10)
            throw new ValidatorException(message);
    }
}
