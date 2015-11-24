package com.rm.pir.controller.student;

import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Student;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class StudentUpdate {

    @Inject
    private Student student;
    @Inject
    private StudentDAO dao;
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(StudentUpdate.class.getName()).log(Level.INFO, "StudentUpdate destroyed");
//    }
    
    public String updateAccount() {
        try {
            student.setHomephone(com.rm.pir.utilities.Util.convertPhone(student.getHomephone()));
            student.setCellphone(com.rm.pir.utilities.Util.convertPhone(student.getCellphone()));
            dao.update(student);
            return "dashboard?faces-redirect=true";
        } catch (SQLException ex) {
            Logger.getLogger(StudentUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
