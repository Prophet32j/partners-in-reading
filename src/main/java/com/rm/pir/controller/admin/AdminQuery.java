package com.rm.pir.controller.admin;

import com.rm.pir.dao.interfaces.UserDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Student;
import com.rm.pir.model.User;
import com.rm.pir.utilities.Constants;
import com.rm.pir.utilities.Util;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.WindowScoped;

@Named
@WindowScoped
public class AdminQuery implements Serializable {
    
    private String criteria;
//    private Connection con;
    private String output;
    private List<Student> students;
    private List<Child> children;
    private List<User> users;
    
    @Resource(name=Constants.DB_JNDI_NAME)
    private DataSource ds;
    @Inject
    private UserDAO udao;
    
    @PostConstruct
    public void start() {
        users = new ArrayList<>();
        students = new ArrayList<>();
        children = new ArrayList<>();
    }
    
    public void clear() {
        criteria = "";
        students.clear();
        children.clear();
        users.clear();
    }
    
    public void search() throws SQLException {
        try (Connection con = ds.getConnection()) {
            String searchString = "%" + Util.sanitize(criteria) + "%";
            // search student table
            try (PreparedStatement search = con.prepareStatement(
                         "SELECT DISTINCT lastname, firstname, email, cellphone "
                         + "FROM normal_partner.student s "
                         + "WHERE s.lastname LIKE ? OR "
                         + "s.firstname LIKE ? OR "
                         + "s.email LIKE ? OR "
                         + "s.college LIKE ?")) {
                search.setString(1, searchString);
                search.setString(2, searchString);
                search.setString(3, searchString);
                search.setString(4, searchString);
                try (ResultSet rs = search.executeQuery()) {
                    students.clear();
                    children.clear();
                    while (rs.next()) {
                        Student bean = new Student();
                        bean.setLastname(rs.getString("lastname"));
                        bean.setFirstname(rs.getString("firstname"));
                        bean.setEmail(rs.getString("email"));
                        bean.setCellphone(rs.getString("cellphone"));
                        students.add(bean);
                    }
                }
            }
            // search child table
            try (PreparedStatement search = con.prepareStatement(
                         "SELECT childid, lastname, firstname, email, cellphone "
                         + "FROM normal_partner.child c "
                         + "WHERE c.lastname LIKE ? OR "
                         + "c.firstname LIKE ? OR "
                         + "c.email LIKE ? OR "
                         + "c.pnt_gdn_one LIKE ? OR "
                         + "c.pnt_gdn_two LIKE ?")) {
                search.setString(1, searchString);
                search.setString(2, searchString);
                search.setString(3, searchString);
                search.setString(4, searchString);
                search.setString(5, searchString);
                try (ResultSet rs = search.executeQuery()) {
                    while (rs.next()) {
                        Child bean = new Child();
                        bean.setChildID(rs.getLong("childid"));
                        bean.setLastname(rs.getString("lastname"));
                        bean.setFirstname(rs.getString("firstname"));
                        bean.setEmail(rs.getString("email"));
                        bean.setCellphone(rs.getString("cellphone"));
                        children.add(bean);
                    }
                }
            }
        }
    }
    
    public void showAllUsers() {
        try {
            users = udao.findAll();
        } catch (SQLException ex) {
            Logger.getLogger(AdminQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> beans) {
        this.students = beans;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }    

    /**
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
