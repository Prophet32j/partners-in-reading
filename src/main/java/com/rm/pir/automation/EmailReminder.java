package com.rm.pir.automation;

import com.rm.pir.model.Settings;
import com.rm.pir.utilities.Mailer;
import com.rm.pir.utilities.Constants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.joda.time.DateTime;

public class EmailReminder implements Runnable {
    
    Connection con;
    
    private DataSource ds;
    
    private String emailBody;
    private List<String> to;
    private String subject;
    private Settings settings;
    
    public EmailReminder() {
        try {
            ds = (DataSource)new InitialContext().lookup(Constants.DB_JNDI_NAME);
        } catch (NamingException ex) {
            Logger.getLogger(AutoPair.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            con = ds.getConnection();
            getSettingsFromDB();
            sendSessionReminders();
            sendSurvey();
        } catch (SQLException ex) {
            Logger.getLogger(EmailReminder.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmailReminder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void getSettingsFromDB() throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "select * from normal_partner.settings");
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                DateTime susp = new DateTime(rs.getDate("suspense_date").getTime());
                DateTime start = new DateTime(rs.getDate("start_date").getTime());
                DateTime end = new DateTime(rs.getDate("end_date").getTime());
                
                settings = new Settings(true,susp,start,end);
            }
        }
    }

    public void sendSurvey() {
        
        DateTime dt = new DateTime();
        int iMoY = dt.getMonthOfYear();
        int iDoM = dt.getDayOfMonth();
        // check if today is Dec 15 or May 15
        if (iMoY == 12 && iDoM == 15 
                || iMoY == 5 && iDoM == 15) {
            try {
                try (PreparedStatement find = con.prepareStatement(
                             "SELECT DISTINCT s.email "
                             + "FROM normal_partner.student s "
                             + "WHERE s.studentid IN "
                             + "(SELECT studentid FROM normal_partner.pairs GROUP BY studentid) OR "
                             + "s.studentid IN "
                             + "(SELECT studentid FROM normal_partner.student_requested_partner)"); 
                        ResultSet rs = find.executeQuery()) {
                    // get all email addresses
                    to = new ArrayList<>();
                    while (rs.next()) {
                        to.add(rs.getString("email"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmailReminder.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            // make sure there are students to send emails to
            if (to.size() > 0) {
                subject = "Partners In Reading - Student Survey";
                emailBody = "<html>"
                        + "<body>"
                        + "Hello and thank you for participating in this semester's "
                        + "Normal Public Library Partners In Reading program.<br/>"
                        + "Please complete the survey, provided by the link bellow, <br/>"
                        +"<a href ='http://partners.normal-library.org/survey.xhtml'>http://partners.normal-library.org/survey.xhtml</a><br/>"
                        + "Thanks again!<br/>"
                        + "The Partners In Reading Team"
                        + "</body></html>";
                sendReminderEmails();
            }
        }
    }
    
    public void sendSessionReminders() {
        DateTime dt = new DateTime();
        
        int iDoW = dt.getDayOfWeek();
        // check to see if program has started and if it's before a reading day
        if (settings.getStartDate().isAfterNow() 
                && settings.getEndDate().isBeforeNow()
                && (iDoW < 4 || iDoW == 7)) {
            try {
                subject = "Partners In Reading - Session Reminder";
                emailBody = 
                        "<html>"
                        + "<body>"
                        + "<h3>Partners In Reading Session</h3>"
                        + "Just a reminder, you have a reading session tomorrow. Here are the details:<br/>"
                        + "<b>Student: </b> %s %s <br/>"
                        + "<b>Child: </b> %s %s <br/>"
                        + "<b>Day: </b> %s <br/>"
                        + "<b>Time: </b> %s <br/>"
                        + "<br/>"
                        + "Thanks again!<br/>"
                        + "The Partners In Reading Team"
                        + "</body>"
                        + "</html>";
                String sql = "select s.firstname as sfn, s.lastname as sln, s.email as semail, c.firstname as cfn, "
                        + "c.lastname as cln, c.email as cemail, p.session_day, p.session_hour "
                        + "from normal_partner.pairs p join normal_partner.student s on p.studentid=s.studentid "
                        + "join normal_partner.child c on c.childid = p.childid "
                        + "where p.session_day = ?";
                switch (iDoW) {
                    case 1: // Monday
                        try (PreparedStatement ps = con.prepareStatement(sql)) {
                           ps.setString(1, "Tuesday");
                           try (ResultSet rs = ps.executeQuery()) {
                               while (rs.next()) {
                                   to.add(rs.getString("semail"));
                                   to.add(rs.getString("cemail"));
                                   emailBody = String.format(emailBody,
                                           rs.getString("sfn"),
                                           rs.getString("sln"),
                                           rs.getString("cfn"),
                                           rs.getString("cln"),
                                           rs.getString("session_day"),
                                           rs.getString("session_hour"));
                                   sendReminderEmails();
                               }
                            }
                        }
                        break;
                    case 2: // Tuesday
                        try (PreparedStatement ps = con.prepareStatement(sql)) {
                           ps.setString(1, "Wednesday");
                           try (ResultSet rs = ps.executeQuery()) {
                               while (rs.next()) {
                                   to.add(rs.getString("semail"));
                                   to.add(rs.getString("cemail"));
                                   emailBody = String.format(emailBody,
                                           rs.getString("sfn"),
                                           rs.getString("sln"),
                                           rs.getString("cfn"),
                                           rs.getString("cln"),
                                           rs.getString("session_day"),
                                           rs.getString("session_hour"));
                                   sendReminderEmails();
                               }
                            }
                        }
                        break;
                    case 3: // Wednesday
                        try (PreparedStatement ps = con.prepareStatement(sql)) {
                           ps.setString(1, "Thursday");
                           try (ResultSet rs = ps.executeQuery()) {
                               while (rs.next()) {
                                   to.add(rs.getString("semail"));
                                   to.add(rs.getString("cemail"));
                                   emailBody = String.format(emailBody,
                                           rs.getString("sfn"),
                                           rs.getString("sln"),
                                           rs.getString("cfn"),
                                           rs.getString("cln"),
                                           rs.getString("session_day"),
                                           rs.getString("session_hour"));
                                   sendReminderEmails();
                               }
                            }
                        }
                        break;
                    case 7: // Sunday
                        try (PreparedStatement ps = con.prepareStatement(sql)) {
                           ps.setString(1, "Monday");
                           try (ResultSet rs = ps.executeQuery()) {
                               while (rs.next()) {
                                   to.add(rs.getString("semail"));
                                   to.add(rs.getString("cemail"));
                                   emailBody = String.format(emailBody,
                                           rs.getString("sfn"),
                                           rs.getString("sln"),
                                           rs.getString("cfn"),
                                           rs.getString("cln"),
                                           rs.getString("session_day"),
                                           rs.getString("session_hour"));
                                   sendReminderEmails();
                               }
                            }
                        }
                        break;
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmailReminder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sendReminderEmails() {    
        new Mailer(to,subject,emailBody).send();
    }
    
    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }
}