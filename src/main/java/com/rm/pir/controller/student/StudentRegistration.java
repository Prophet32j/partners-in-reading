package com.rm.pir.controller.student;

import com.rm.pir.utilities.Mailer;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Student;
import com.rm.pir.model.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class StudentRegistration {

    @Inject
    private User user;
    @Inject
    private Student student;
    @Inject
    private StudentDAO dao;
    
    private Student newStudent;
    private boolean hasReqPartner;

    @PostConstruct
    public void create() {
        newStudent = new Student();
    }
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(StudentRegistration.class.getName()).log(Level.INFO, "StudentRegistration destroyed");
//    }

    public String register() {
        try {
            newStudent.setHomephone(com.rm.pir.utilities.Util.convertPhone(newStudent.getHomephone()));
            newStudent.setCellphone(com.rm.pir.utilities.Util.convertPhone(newStudent.getCellphone()));
            newStudent.setFirst_time(true);
            newStudent.setOrtn_complete(false);
            newStudent.setBckgrnd_check_complete(false);
            newStudent.setEmail(user.getEmail());
            long key = dao.insert(newStudent);
            if (key > 0) {
                //now set the studentbean
                newStudent.setStudentID(key);
                student.copyStudent(newStudent);
                try {
                    sendConfirmation();
                    sendOrientationInfo();
                } catch (Exception ex) {
                    Logger.getLogger(StudentRegistration.class.getName()).log(Level.SEVERE, null, ex);
                }
                return hasReqPartner ? "request-partner?faces-redirect=true" : 
                        "edit-times?faces-redirect=true";
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentRegistration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student getNewStudent() {
        return newStudent;
    }

    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }

    public void sendConfirmation() {
        String to = user.getEmail();
        String subject = "Welcome to Partners In Reading. Thank you for registering!";
        String body =
                "<html>"
                + "<body><center>"
                + "<h3>Thank you for registering!</h3>"
                + "<p>If you haven't done so already, don't forget to submit your "
                + "availability to us. You can do so through the student dashboard</p>"
                + "<h4>Account Details</h4>"
                + "<table>"
                + "<tr>"
                + "<td>Email: </td>"
                + "<td>" + user.getEmail() + "</td>"
                + "</tr>"
                + "<tr>		"
                + "<td>First Name: </td>"
                + "<td>" + newStudent.getFirstname() + "</td>"
                + "</tr>"
                + "<tr>		"
                + "<td>Last Name: </td>"
                + "<td>" + newStudent.getLastname() + "</td>"
                + "</tr>"
                + "<tr>		"
                + "<td>College: </td>"
                + "<td>" + newStudent.getCollege() + "</td>"
                + "</tr>"
                + "<tr>		"
                + "<td>Gender: </td>"
                + "<td>" + newStudent.getGender() + "</td>"
                + "</tr>"
                + "<tr>		"
                + "<td>Home Phone: </td>"
                + "<td>" + newStudent.getHomephone() + "</td>"
                + "</tr>"
                + "<tr>		"
                + "<td>Cell Phone: </td>"
                + "<td>" + newStudent.getCellphone() + "</td>"
                + "</tr>"
                + "</table>"
                + "</center></body>"
                + "</html>";
        Mailer email = new Mailer(to, subject, body);
        email.send();
    }

    public void sendOrientationInfo() {
        String to = user.getEmail();
        String subject = "Partners in Reading Orientation";
        String body =
                "<html>"
                + "<body>"
                + "<p>Greetings!<br/><br/>"
                + "You are receiving this email because you registered as a new partner for the Partners in Reading program at the Normal Public Library.  If you have received this email in error and are a returning student, please delete and send me a reply, thank you!<br/><br/>"
                + "Good Afternoon! So glad that you have signed up for the program. Please be sure to read through all of the material in this email. !<br/><br/>"
                + "The next step for you is to login to your account and complete the orientation (Click the “Orientation” button on the left side of the page once you are logged in). Please watch the video and fill out the quiz after watching the video.  The video is about 7 minutes long and the form has 7 questions.  The whole process should take you about 20 minutes at most.  Once you have completed orientation I will send you your partners information. <br/><br/>"
                + "<b>***PLEASE MAKE SURE TO READ!  In the video, it refers to a folder that contains different materials.  Those materials are contained in this email and you will receive your folder when you check in.</b> <br/><br/>"
                + "I have also attached our dress code and our guidelines.  These are covered in the video, but this way you also have a paper copy.  My contact information for any questions or issues is also on the guideline sheet.<br/><br/>"
                + "<b>***IMPORTANT: The following link is to our volunteer application.  Attached above is our background check please print, fill out, and drop off both of these at the library. </b> ****  You can drop them off anytime the library is open and just ask that they be delivered to Lyndsey Carney.  (Library Hours are below) <br/><br/>"
                + "<a href='https://docs.google.com/viewer?a=v&pid=sites&srcid=ZGVmYXVsdGRvbWFpbnxpdDM1M2lzdXxneDozYTFmMjIwZGE2YTc4ZmY4'>Background Check</a><br/><br/>"
                + "Below are some informational documents as well as a script for you to use when contacting your partner. Please become familiar with the pages below.<br/>"
                + "<a href='https://docs.google.com/viewer?a=v&pid=sites&srcid=ZGVmYXVsdGRvbWFpbnxpdDM1M2lzdXxneDoxMWFiODVjOTM4M2JiYjBk'>Dress Code</a><br/><br/>"
                + "<a href='https://docs.google.com/viewer?a=v&pid=sites&srcid=ZGVmYXVsdGRvbWFpbnxpdDM1M2lzdXxneDo2Y2E0NDhhMDZiM2FhOTc3'>Orientation Handout</a><br/><br/>"
                + "<a href='https://docs.google.com/file/d/0By_x67Zm9fjMc1Zfd1JjZTVWREE'>Partner's Script</a><br/><br/>"
                + "<a href='http://www.normalpl.org/files/volunteer.pdf'>Volunteer Application</a><br/><br/>"
                + "Once you receive your partners information, please call them asap.  There is a script attached to this email.  Please send me a confirmation email letting me know if you reached them, left a voice mail, or had any other issues.  They are expecting your phone calls asap! <br/><br/>"
                + "I am so glad you are in the program.  Let me know if you have any questions or issues! <br/><br/>"
                + "Lyndsey Carney<br/><br/>"
                + "Partners in Reading Coordinator<br/><br/>"
                + "Normal Public Library<br/><br/>"
                + "(309)454-4668<br/><br/>"
                + "<table>"
                + "<th> Library Hours</th>"
                + "<tr>"
                + "<td> Sunday 1-5</td>"
                + "</tr>"
                + "<tr>"
                + "<td> Monday 9-9</td>"
                + "</tr>"
                + "<tr>"
                + "<td> Tuesday 9-9</td>"
                + "</tr>"
                + "<tr>"
                + "<td> Wednesday 9-9</td>"
                + "</tr>"
                + "<tr>"
                + "<td> Thursday 9-9</td>"
                + "</tr>"
                + "<tr>"
                + "<td> Friday 9-5</td>"
                + "</tr>"
                + "<tr>"
                + "<td> Saturday 9-5</td>"
                + "</tr>"
                + "</table>"
                + "</p>"
                + "</body>"
                + "</html>";
                
        Mailer email = new Mailer(to, subject, body);
        email.send(); 
    }

    /**
     * @return the hasReqPartner
     */
    public boolean isHasReqPartner() {
        return hasReqPartner;
    }

    /**
     * @param hasReqPartner the hasReqPartner to set
     */
    public void setHasReqPartner(boolean hasReqPartner) {
        this.hasReqPartner = hasReqPartner;
    }
}
