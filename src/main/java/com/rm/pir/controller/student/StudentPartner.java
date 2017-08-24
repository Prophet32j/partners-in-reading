package com.rm.pir.controller.student;

import com.rm.pir.dao.interfaces.StudentPartnerDAO;
import com.rm.pir.model.Partner;
import com.rm.pir.model.Student;
import com.rm.pir.utilities.Util;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

@Named
@ViewAccessScoped
public class StudentPartner implements Serializable {
    
    @Inject
    private Student student;
    @Inject
    private StudentPartnerDAO spdao;
    
    private List<Partner> partners;
    private Partner partner;
    private boolean requesting;
    
    @PostConstruct
    public void init() {
        try {
            partners = spdao.findByID(student.getStudentID());
        } catch (SQLException ex) {
            Logger.getLogger(StudentPartner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(StudentPartner.class.getName()).log(Level.INFO, "StudentPartner destroyed");
//    }
    
    public void delete(Partner partner) {
        try {
            spdao.delete(partner);
            for (int i=0; i<partners.size(); i++) {
                if (partners.get(i).getID() == partner.getID()) {
                    partners.remove(i);
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentPartner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addRequest() {
        partner.setID(student.getStudentID());
        partner.setPhone(Util.convertPhone(partner.getPhone()));
        try {
            spdao.insert(partner);
            requesting = false;
        } catch (SQLException ex) {
            Logger.getLogger(StudentPartner.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (student.getDaymap() == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("edit-times.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(StudentPartner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void newRequest() {
        partner = new Partner();
        requesting = true;
    }

    /**
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * @return the partners
     */
    public List<Partner> getPartners() {
        return partners;
    }

    /**
     * @param partners the partners to set
     */
    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

    /**
     * @return the requesting
     */
    public boolean isRequesting() {
        return requesting;
    }

    /**
     * @param requesting the requesting to set
     */
    public void setRequesting(boolean requesting) {
        this.requesting = requesting;
    }
}
