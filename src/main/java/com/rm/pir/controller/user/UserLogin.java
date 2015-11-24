package com.rm.pir.controller.user;

import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.StudentAvailDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Family;
import com.rm.pir.model.Student;
import com.rm.pir.model.User;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.mindrot.jbcrypt.BCrypt;

@Named
@SessionScoped
public class UserLogin implements Serializable {

    @Inject 
    private UserDAO dao;
    @Inject 
    private StudentDAO sdao;
    @Inject 
    private ChildDAO cdao;
    @Inject 
    private StudentAvailDAO savail;
    
    @Inject 
    private User userbean;
    
    private int attempts;
    private String password;
    private String email;

    @PostConstruct
    public void init() {
        attempts = 0;
    }

    public String login() {
        //make sure they haven't tried too much
        if (attempts >= 3) {
            return "401?faces-redirect=true";
        }
        User user = null;
        try {
            user = dao.findByEmail(email);
        } catch (SQLException ex) {
            Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        /* if value returned
         * validate password
         * if password is valid, forward to account page
         */
        if (user != null) {
            if (BCrypt.checkpw(password, user.getPassword()) && user.isActivated()) {
                //user is verified, log user in, set userbean
                userbean.copyUser(user);
                try {
                    // update last login to reflect this login
                    dao.updateLoginTimestamp(user);
                } catch (SQLException ex) {
                    Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
                String type = user.getAcct_type();
                switch (type) {
                    case "f":
                        return "frontDesk/dashboard?faces-redirect=true";
                    case "a":
                        return "admin/dashboard?faces-redirect=true";
                    case "s":
                        try {
                            //make sure registration was completed
                            Student s = sdao.findByEmail(user.getEmail());
                            if (s == null) 
                                return "student/register?faces-redirect=true";  // complete registration process
                            else {
                                setStudentSession(s);
                                return "student/dashboard?faces-redirect=true";
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        } catch (NamingException ex) {
                            Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    case "c":
                        try {
                            // make sure registration was completed
                            List<Child> list = cdao.findByEmail(user.getEmail());
                            setFamilySession(list);
                            if (list.isEmpty())
                                return "child/register?faces-redirect=true";
                            else 
                                return "child/dashboard?faces-redirect=true";
                        } catch (SQLException ex) {
                            Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        } catch (NamingException ex) {
                            Logger.getLogger(UserLogin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
            }
            else if (!user.isActivated()) {
                return "activate?faces-redirect=true";
            }
        }
        // username needs to go back to null
        email = null;
        // if password is incorrect, increase number of attempts and check if attempts > 3 if not return null   
        attempts++;
        if (attempts >= 3) {
            return "401?faces-redirect=true";
        }
        return "loginBad";
    }
    
    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
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
    
    private void setFamilySession(List<Child> list) throws NamingException {
        Family family = lookup(Family.class);
        family.setFamlist(list);
    }
    
    private void setStudentSession(Student s) throws SQLException, NamingException {
        s.setDaymap(savail.findByID(s.getStudentID()));
        Student student = lookup(Student.class);
        student.copyStudent(s);
    }
    
    private <T> T lookup(Class<T> beanClass) throws NamingException {
        BeanManager bm = getBeanManager();
        Bean<T> bean = (Bean<T>) bm.getBeans(beanClass).iterator().next();
        CreationalContext<T> context = bm.createCreationalContext(bean);
        T t = (T) bm.getReference(bean, beanClass, context);
        return t;
    }
    
    private BeanManager getBeanManager() throws NamingException {
        return (BeanManager) InitialContext.doLookup("java:comp/BeanManager");
    }
}
