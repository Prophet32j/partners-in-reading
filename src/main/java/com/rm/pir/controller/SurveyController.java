package com.rm.pir.controller;

import com.rm.pir.utilities.MailSender;
import com.rm.pir.model.Survey;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class SurveyController {
    
    @Inject
    private Survey survey;

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
    
    public String sendSurveyResults(){
        String to = "partnersinreading@normalpl.org";
        String subject="Survey Response";           
        
        String temp="";
        for(int i=0;i<survey.getQuestionTwo().length;i++){
            temp += survey.getQuestionTwo()[i]+";";
        }
        
        String temp2="";
        for(int i=0;i<survey.getQuestionFour().length;i++){
            temp2 += survey.getQuestionFour()[i]+";";
        }
        
        String temp3="";
        for(int i=0;i<survey.getQuestionEight().length;i++){
            temp2 += survey.getQuestionEight()[i]+";";
        }
        
        String body = 
                "<html>"
                + "<center><body>"
                + "<h3>Survey Details</h3>"
                + "<table>"
                    + "<tr>"
                        + "<td>What category does your major fall under? </td>"
                        + "<td>"+survey.getQuestionOne()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How did you hear about the program?</td>"
                        + "<td>"+temp+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How many semesters have you participated in Partners in Reading? </td>"
                        + "<td>"+survey.getQuestionThree()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>Why did you join Partners in Reading? </td>"
                        + "<td>"+temp2+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>Would you recommend Partners in Reading to someone else? </td>"
                        + "<td>"+survey.getQuestionFive()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>Do you plan to participate in Partners in Reading next semester?</td>"
                        + "<td>"+survey.getQuestionSix()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How would you summarize your experience?</td>"
                        + "<td>"+survey.getQuestionSeven()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>Did you ever have any problems with your partner?</td>"
                        + "<td>"+temp3+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How well were the following goals for Partners in Reading met?<br>"
                        + "Feeling like you've made an impact</td>"
                        + "<td>"+survey.getQuestionNine()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How well were the following goals for Partners in Reading met?<br>"
                        +"Finding books you like to read</td>"
                        + "<td>"+survey.getQuestionTen()+"</td>"
                    + "</tr>"
                     + "<tr>		"
                        + "<td>How well were the following goals for Partners in Reading met?<br>"
                        +"Improving your ability to work with children</td>"
                        + "<td>"+survey.getQuestionEleven()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How well were the following goals for Partners in Reading met?<br>"
                        +"Friendly atmosphere</td>"
                        + "<td>"+survey.getQuestionTwelve()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How well were the following goals for Partners in Reading met?<br>"
                        +"Enjoyable sessions</td>"
                        + "<td>"+survey.getQuestionThirteen()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How well were the following goals for Partners in Reading met?<br>"
                        +"Familiarizing yourself with the resources of the library</td>"
                        + "<td>"+survey.getQuestionFourteen()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How organized do you feel the program was?</td>"
                        + "<td>"+survey.getQuestionFifteen()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>How well do you feel your orientation prepared you for Partners in Reading?</td>"
                        + "<td>"+survey.getQuestionSixteen()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>Did the tour during your orientation help you to find library resources during your Partners in Reading sessions? </td>"
                        + "<td>"+survey.getQuestionSeventeen()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>Did you drive to Partners in Reading?</td>"
                        + "<td>"+survey.getQuestionEighteen()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>Do you have any suggestions for how to make orientation better?</td>"
                        + "<td>"+survey.getQuestionNineteen()+"</td>"
                    + "</tr>"
                    + "<tr>		"
                        + "<td>We really appreciate your input on Partners in Reading. Please let us know any thoughts that you have on the program.</td>"
                        + "<td>"+survey.getQuestionTwenty()+"</td>"
                    + "</tr>"
                + "</table>"
            + "</body></center>"
   + "</html>";
                
        MailSender email = new MailSender(to,subject,body);
        email.send();
        
        return "survey-confirmation";
    }
}
