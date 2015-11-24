/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class Family implements Serializable {
    
    private List<Child> famlist;

    @PostConstruct
    public void init() {
        famlist = new ArrayList<Child>();
    }
    
    public List<Child> getFamlist() {
        return famlist;
    }

    public void setFamlist(List<Child> famlist) {
        this.famlist = famlist;
    }
}