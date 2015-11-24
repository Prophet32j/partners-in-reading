package com.rm.pir.controller;

import com.rm.pir.utilities.MailSender;
import com.rm.pir.dao.interfaces.StudentDAO;
import com.rm.pir.model.Student;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@Named
@RequestScoped
public class QuizController {

    private String q1_a;
    private String q2_a;
    private String q3_a;
    private String q4_a;
    private String q5_a;
    private String q6_a;
    private String q7_a;
    private int numCorrect = 0;
    
//    @Inject 
//    private User user;
    @Inject
    private Student student;
    @Inject
    private StudentDAO dao;
    
//    @PreDestroy
//    public void destroy() {
//        Logger.getLogger(QuizController.class.getName()).log(Level.INFO, "QuizController destoyed");
//    }

    public String gradeQuiz() {
        //grade each of the quiz questions and increment numCorrect if the answer is correct
        if (q1_a.equals("At the Children’s Information Desk")) 
            numCorrect++;
        if (q2_a.equals("free time.")) 
            numCorrect++;
        if (q3_a.equals("has a chance to read out loud.")) 
            numCorrect++;
        if (q4_a.equals("In the Children’s Department on the second floor of the library.")) 
            numCorrect++;
        if (q5_a.equals("A coupon from a local business."))
            numCorrect++;
        if (q6_a.equals("Shorts")) 
            numCorrect++;
        if (q7_a.equals("All the above")) 
            numCorrect++;
                  
        student.setOrtn_complete(true);
        student.setFirst_time(false);
        try {
            dao.updateOrientation(student);
            //sends the email with the quiz results to the current student's email
            sendQuizResults();
            return "dashboard?faces-redirect=true";
        } catch (SQLException ex) {
            Logger.getLogger(QuizController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
    public void sendQuizResults(){
        String to = student.getEmail();
        String subject="Partners In Reading - Orientation Quiz Results";
        
        String body = "Quiz Results for " + student.getFirstname() + " " + student.getLastname()
      + "<html>"
                + "<center><body>"
                    + "<h3>Quiz Results: " + numCorrect + " out of 7</h3><br/>"
                    + "<table>"
                    + "<tr>"
                        + "<td>1. Where should you check in and meet your partner upon arriving at the library?</td><br/>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Your response:</b> " + q1_a + "</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Correct Answer:</b> At the Children’s Information Desk</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td>2. Please complete this sentence: The last 15 minutes of your session is for </td><br/>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Your response:</b> " + q2_a + "</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Correct Answer:</b> free time.</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td>3. Please complete this sentence.  It doesn't matter how you decide to read, as long as your partner</td><br/>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Your response:</b> " + q3_a + "</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<b><td>Correct Answer:</b> has a chance to read out loud.</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td>4. We ask all parents to drop off and pick up their children where?</td><br/>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Your response:</b> " + q4_a + "</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Correct Answer:</b>In the Children’s Department on the second floor of the library.</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td>5. What does a child receive when they have finished the Reading Challenge?</td><br/>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Your response:</b> " + q5_a + "</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<b><td>Correct Answer:</b> A coupon from a local business.</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td>6. What is one item of clothing you may NOT wear to participate in Partners in Reading?</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Your response:</b> " + q6_a + "</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Correct Answer:</b> Shorts.</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td>7. What is one way you can contact the library if a problem arises?</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Your response:</b> " + q7_a + "</td>"
                    + "</tr>"
                    + "<tr>"
                        + "<td><b>Correct Answer:</b> All the above</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td><br/>You have now completed orientation. Thank you for participating in the Partners in Reading program!</td>"
                    + "</tr>"
                    + "</table>"
            + "</body></center>"
   + "</html>";
                
        MailSender email = new MailSender(to,subject,body);
        email.send();
    }
            
    public String getQ1_a() {
        return q1_a;
    }

    public void setQ1_a(String q1_a) {
        this.q1_a = q1_a;
    }

    public String getQ2_a() {
        return q2_a;
    }

    public void setQ2_a(String q2_a) {
        this.q2_a = q2_a;
    }

    public String getQ3_a() {
        return q3_a;
    }

    public void setQ3_a(String q3_a) {
        this.q3_a = q3_a;
    }

    public String getQ4_a() {
        return q4_a;
    }

    public void setQ4_a(String q4_a) {
        this.q4_a = q4_a;
    }

    public String getQ5_a() {
        return q5_a;
    }

    public void setQ5_a(String q5_a) {
        this.q5_a = q5_a;
    }

    public String getQ6_a() {
        return q6_a;
    }

    public void setQ6_a(String q6_a) {
        this.q6_a = q6_a;
    }

    public String getQ7_a() {
        return q7_a;
    }

    public void setQ7_a(String q7_a) {
        this.q7_a = q7_a;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public void setNumCorrect(int numCorrect) {
        this.numCorrect = numCorrect;
    }
}
