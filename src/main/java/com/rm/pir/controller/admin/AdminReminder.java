package com.rm.pir.controller.admin;

import com.rm.pir.utilities.Mailer;
import com.rm.pir.dao.interfaces.ChildDAO;
import com.rm.pir.dao.interfaces.StudentDAO;
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
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

@Named
@ViewAccessScoped
public class AdminReminder implements Serializable {
    
    @Inject StudentDAO sdao;
    @Inject ChildDAO cdao;
    
    @Resource(name=Constants.DB_JNDI_NAME)
    private DataSource ds;
    
    private String recipients;
    private String reminder;
    private String body;
    private String subject;
    private List<String> to;
    private boolean sending;
    
    @PostConstruct
    public void init() {
        sending = false;
    }
    
    public void changeReminder() {
        if (reminder.equals("off")) {
            body = "Just a reminder, we are still having Partners in Reading sessions today."
                    + "If you cannot make your session, please make sure to contact your partner!<br/>"
                    + "Thanks!";
            setSubject("Reminder About Upcoming Break");
        }
        else if (reminder.equals("break")) {
            body = "Hello Partners!<br/> "
                    + "Just a reminder that this week is Unit 5's Spring Break and we will have Partners sessions this week. "
                    + "If you are unable to make your session, please let your partner know. "
                    + "If you have any concern about your partner coming to your session, please go ahead and call them to verify that you will be meeting.<br/> "
                    + "See you this week!";
            setSubject("Reminder About Day Off");
        }
        else if (reminder.equals("semester")) {
            body = "Hello Partners!<br/> "
                    + "Insert Message Here";
            setSubject("Reminder About Upcoming End of Semester");
        }
        else if (reminder.equals("dress")) {
            body = "Hello Partners!<br/> "
                    + "As the weather gets warmer I wanted to send out a reminder email in regards to the dress code. "
                    + "The weather outside today is beautiful!  I know a lot of you will be wearing shorts to your classes today, "
                    + "please remember though, that shorts and tank tops are not appropriate for your sessions. "
                    + "If you have any other questions about the dress code, please check the information sheet from your orientation or send me an email!<br/> "
                    + "Thanks for your cooperation with this and for the time that you volunteer!";
            setSubject("Reminder About Dress Code Policy");
        }
        else if (reminder.equals("holiday")) {
            body = "Just a reminder about [insert holiday],<br/>"
                    + "We are still having Partners in Reading sessions.<br/>"
                    + "If you cannot make your session, please make sure to contact your partner!<br/>"
                    + "Thanks!";
            setSubject("Reminder About Upcoming Holiday");
        }
        else if (reminder.equals("blank")) {
            body = "";
            setSubject("");
        }
    }
    
    public void sendEmail() {
        try {
            sending = true;
            Mailer mailer;
            //get the correct emails based on user choice
            to = getEmailAddresses();
            mailer = new Mailer(to, getSubject(), body);
            mailer.send();
            
            recipients = null;
            reminder = null;
            body = "";
            subject = "";
            sending = false;
        } catch (SQLException ex) {
            Logger.getLogger(AdminReminder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private List<String> getEmailAddresses() throws SQLException {
        List<String> all = new ArrayList<>();
        if (recipients.equals("students")) {
            List<String> emails = sdao.findAllAvailableStudentEmails();
            all.addAll(emails);
        } else if (recipients.equals("children")) {
            List<String> emails = cdao.findAllAvailableChildEmails();
            all.addAll(emails);
        } else if (recipients.equals("pairs")) {
            try (Connection con = ds.getConnection(); 
                    PreparedStatement find = con.prepareStatement(
                                    "select distinct s.email " +
                                    "from normal_partner.child s join normal_partner.pairs p " +
                                    "on s.childid=p.childid " +
                                    "union all " +
                                    "select distinct s.email " +
                                    "from normal_partner.student s join normal_partner.pairs p " +
                                    "on s.studentid=p.studentid"); 
                    ResultSet rs = find.executeQuery()) {
                while (rs.next()) 
                    all.add(rs.getString("email"));
            }
        } else if (recipients.equals("all")) {
            try (Connection con = ds.getConnection(); 
                    PreparedStatement find = con.prepareStatement(
                                    "select distinct email " +
                                    "from normal_partner.student " +
                                    "where studentid in " +
                                    "(SELECT studentid FROM normal_partner.availability_student) " +
                                    "union all " +
                                    "select distinct email " +
                                    "from normal_partner.child " +
                                    "where childid in " +
                                    "(SELECT childid FROM normal_partner.availability_child)"); 
                    ResultSet rs = find.executeQuery()) {
                while (rs.next()) {
                    all.add(rs.getString("email"));
                }
            }
        }
        return all;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the sending
     */
    public boolean isSending() {
        return sending;
    }

    /**
     * @param sending the sending to set
     */
    public void setSending(boolean sending) {
        this.sending = sending;
    }
    
}
