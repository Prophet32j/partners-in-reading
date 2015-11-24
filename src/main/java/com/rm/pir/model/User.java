/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class User implements Serializable {

    //variables for user table
    private String email;
    private String password;
    private String acct_type;
    private Date created;
    private Timestamp last_login;
    private boolean activated;
    private String activationKey;
    
    @PostConstruct
    public void onCreate() {
        email = null;
        password = null;
        created = null;
        acct_type = null;
        activated = false;
        activationKey = null;
    }
    
    public void copyUser(User user) {
        this.email = user.email;
        this.password = user.password;
        this.acct_type = user.acct_type;
        this.created = user.created;
        this.last_login = user.last_login;
        this.activated = user.activated;
        this.activationKey = user.activationKey;
    }
    
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAcct_type() {
        return acct_type;
    }

    public void setAcct_type(String acct_type) {
        this.acct_type = acct_type;
    }

    /**
     * @return the last_login
     */
    public Timestamp getLast_login() {
        return last_login;
    }

    /**
     * @param last_login the last_login to set
     */
    public void setLast_login(Timestamp last_login) {
        this.last_login = last_login;
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
     * @return the activation_key
     */
    public String getActivationKey() {
        return activationKey;
    }

    /**
     * @param activationKey the activation_key to set
     */
    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }
}
