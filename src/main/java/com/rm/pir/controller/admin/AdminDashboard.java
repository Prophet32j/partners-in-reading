package com.rm.pir.controller.admin;

import com.rm.pir.utilities.Mailer;
import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.PendingDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Child;
import com.rm.pir.model.Session;
import com.rm.pir.model.Student;
import com.rm.pir.utilities.Constants;
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
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

@Named
@ViewAccessScoped
public class AdminDashboard implements Serializable {
    
    @Inject 
    private PendingDAO pdao;
    @Inject 
    private ChildDAO cdao;
    @Inject
    private StudentDAO sdao;
    
    @Resource(name=Constants.DB_JNDI_NAME)
    private DataSource ds;
    
    private List<Session> pendlist;
//    private Connection con;
    private List<Child> unpaired;
    private List<Student> backgrounds;
    
    @PostConstruct
    private void init() {
        try {
            backgrounds = new ArrayList<>();
            unpaired = new ArrayList<>();
            pendlist = pdao.findAll();
            findUnpaired();
            findIncompleteBackgrounds();
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    private void open() throws SQLException {
//        con = ds.getConnection();
//    }
//
//    private void close() throws SQLException {
//        con.close();
//    }
    
    private void findIncompleteBackgrounds() throws SQLException {
        if (backgrounds == null)
            backgrounds = new ArrayList<>();
        else
            backgrounds.clear();
        try (Connection con = ds.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(
                                "SELECT studentid "
                                + "FROM normal_partner.student s "
                                + "WHERE s.bckgrnd_check_complete = ?")) {
            stmt.setBoolean(1, false);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = sdao.findByID(rs.getLong("studentid"));
                    backgrounds.add(student);
                }
            }
        }
    }
    
    private void findUnpaired() throws SQLException {
        try (Connection con = ds.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(
                                "SELECT childid "
                                + "FROM normal_partner.child c "
                                + "WHERE c.childid NOT IN "
                                + "(SELECT childid FROM normal_partner.pairs) "
                                + "AND c.childid NOT IN "
                                + "(SELECT childid FROM normal_partner.pending)"); 
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int childid = rs.getInt("childid");
                Child child = cdao.findByID(childid);
                unpaired.add(child);
            }
        }
    }
    
    public void accept(Session session) throws SQLException {
        pdao.transfer(session);
        sendConfirmation(session);
        pendlist.remove(session);
    }
    
    public void reject(Session session) throws SQLException {
        pdao.delete(session);
        pendlist.remove(session);
    }
    
    public void acceptAll() throws SQLException {
        for (int i=0; i<pendlist.size(); i++) {
            pdao.transfer(pendlist.get(i));
            sendConfirmation(pendlist.get(i));
        }
        pendlist.clear();
    }
    
    public void rejectAll() throws SQLException {
        for (int i=0; i<pendlist.size(); i++)
            pdao.delete(pendlist.get(i));
        pendlist.clear();
    }
    
    public void markComplete() {
        for (Student student : backgrounds) {
            if (student.isBckgrnd_check_complete()) {
               try {
//                    student.setBckgrnd_check_complete(true);
                    sdao.updateBackgroundCheck(student);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }                 
            }
            
        }
        try {
            findIncompleteBackgrounds();
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendConfirmation(Session session) {
        Student ps = session.getStudent();
        Child pc = session.getChild();
        
        List<String> to = new ArrayList<>();
        to.add(ps.getEmail());
        to.add(pc.getEmail());
        
        String subject = "Partners In Reading - You have been paired!";
        String body =
                "<html>"
                + "<center><body>"
                + "<h3>Pairing Information</h3>"
                + "<p>Don't forget to contact each other for information</p>"
                + "<p>Student's Name: " + ps.getFirstname() + " " + ps.getLastname() + "<br/>"
                + "Student's email: " + ps.getEmail() + "<br/>"
                + "Student's cellphone: " + ps.getCellphone() + "<br/>"
                + "<br/>"
                + "Child's Name: " + pc.getFirstname() + " " + pc.getLastname() + "<br/>"
                + "Child's Parent's Name: " + pc.getParent_one() + "<br/>"
                + "Child's email: " + pc.getEmail() + "<br/>"
                + "Child's cellphone: " + pc.getCellphone() + "<br/>"
                + "Child's homephone: " + pc.getHomephone() + "</p>"
                + "Here is your session time:" + "<br/>"
                + "Day: " + session.getDay() + "<br/>"
                + "Time: " + session.getHour()
                + "</body></center>"
                + "</html>";
        new Mailer(to, subject, body).send();
    }

    public List<Session> getPendlist() {
        return pendlist;
    }

    public void setPendlist(List<Session> pendlist) {
        this.pendlist = pendlist;
    }

    public List<Child> getUnpaired() {
        return unpaired;
    }

    public void setUnpaired(List<Child> unpaired) {
        this.unpaired = unpaired;
    }

    /**
     * @return the backgrounds
     */
    public List<Student> getBackgrounds() {
        return backgrounds;
    }

    /**
     * @param backgrounds the backgrounds to set
     */
    public void setBackgrounds(List<Student> backgrounds) {
        this.backgrounds = backgrounds;
    }
}