/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.validators;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value = "surveyValidator")
@RequestScoped
public class SurveyValidator {

    private String   questionOne;
    private String[] questionTwo;
    private String   questionThree;
    private String[] questionFour;
    private String   questionFive;
    private String   questionSix;
    private String   questionSeven;
    private String[] questionEight;
    private String   questionNine;
    private String   questionTen;
    private String   questionEleven;
    private String   questionTwelve;
    private String   questionThirteen;
    private String   questionFourteen;
    private String   questionFifteen;
    private String   questionSixteen;
    private String   questionSeventeen;
    private String   questionEighteen;
    private String   questionNineteen;
    private String   questionTwenty;
    
    public SurveyValidator() {
        
    }
    
    public SurveyValidator(String questionOne, String[] questionTwo, String questionThree, String[] questionFour, String questionFive, String questionSix, String questionSeven, String[] questionEight,String questionNine,String questionTen, String questionEleven, String questionTwelve, String questionThirteen, String questionFourteen, String questionFifteen, String questionSixteen, String questionSeventeen, String questionEighteen, String questionNineteen, String questionTwenty) {
        this.questionOne=questionOne;
        this.questionTwo=questionTwo;
        this.questionThree=questionThree;
        this.questionFour=questionFour;
        this.questionFive=questionFive;
        this.questionSix=questionSix;
        this.questionSeven=questionSeven;
        this.questionEight=questionEight;
        this.questionNine=questionNine;
        this.questionTen=questionTen;
        this.questionEleven=questionEleven;
        this.questionTwelve=questionTwelve;
        this.questionThirteen=questionThirteen;
        this.questionFourteen=questionFourteen;
        this.questionFifteen=questionFifteen;
        this.questionSixteen=questionSixteen;
        this.questionSeventeen=questionSeventeen;
        this.questionEighteen=questionEighteen;
        this.questionNineteen=questionNineteen;
        this.questionTwenty=questionTwenty;
    }
    
    public String getQuestionOne() {
        return questionOne;
    }

    public void setQuestionOne(String questionOne) {
        this.questionOne = questionOne;
    }

    public String[] getQuestionTwo() {
        return questionTwo;
    }

    public void setQuestionTwo(String[] questionTwo) {
        this.questionTwo = questionTwo;
    }

    public String getQuestionThree() {
        return questionThree;
    }

    public void setQuestionThree(String questionThree) {
        this.questionThree = questionThree;
    }

    public String[] getQuestionFour() {
        return questionFour;
    }

    public void setQuestionFour(String[] questionFour) {
        this.questionFour = questionFour;
    }

    public String getQuestionFive() {
        return questionFive;
    }

    public void setQuestionFive(String questionFive) {
        this.questionFive = questionFive;
    }

    public String getQuestionSix() {
        return questionSix;
    }

    public void setQuestionSix(String questionSix) {
        this.questionSix = questionSix;
    }

    public String getQuestionSeven() {
        return questionSeven;
    }

    public void setQuestionSeven(String questionSeven) {
        this.questionSeven = questionSeven;
    }

    public String[] getQuestionEight() {
        return questionEight;
    }

    public void setQuestionEight(String[] questionEight) {
        this.questionEight = questionEight;
    }

    public String getQuestionNine() {
        return questionNine;
    }

    public void setQuestionNine(String questionNine) {
        this.questionNine = questionNine;
    }

    public String getQuestionTen() {
        return questionTen;
    }

    public void setQuestionTen(String questionTen) {
        this.questionTen = questionTen;
    }

    public String getQuestionEleven() {
        return questionEleven;
    }

    public void setQuestionEleven(String questionEleven) {
        this.questionEleven = questionEleven;
    }

    public String getQuestionTwelve() {
        return questionTwelve;
    }

    public void setQuestionTwelve(String questionTwelve) {
        this.questionTwelve = questionTwelve;
    }

    public String getQuestionThirteen() {
        return questionThirteen;
    }

    public void setQuestionThirteen(String questionThirteen) {
        this.questionThirteen = questionThirteen;
    }

    public String getQuestionFourteen() {
        return questionFourteen;
    }

    public void setQuestionFourteen(String questionFourteen) {
        this.questionFourteen = questionFourteen;
    }

    public String getQuestionFifteen() {
        return questionFifteen;
    }

    public void setQuestionFifteen(String questionFifteen) {
        this.questionFifteen = questionFifteen;
    }

    public String getQuestionSixteen() {
        return questionSixteen;
    }

    public void setQuestionSixteen(String questionSixteen) {
        this.questionSixteen = questionSixteen;
    }

    public String getQuestionSeventeen() {
        return questionSeventeen;
    }

    public void setQuestionSeventeen(String questionSeventeen) {
        this.questionSeventeen = questionSeventeen;
    }

    public String getQuestionEighteen() {
        return questionEighteen;
    }

    public void setQuestionEighteen(String questionEighteen) {
        this.questionEighteen = questionEighteen;
    }

    public String getQuestionNineteen() {
        return questionNineteen;
    }

    public void setQuestionNineteen(String questionNineteen) {
        this.questionNineteen = questionNineteen;
    }

    public String getQuestionTwenty() {
        return questionTwenty;
    }

    public void setQuestionTwenty(String questionTwenty) {
        this.questionTwenty = questionTwenty;
    }
    
//    public void validateQuestions(FacesContext fc, UIComponent ui, Object value) {
//        FacesMessage message = new FacesMessage("Must fill out this question");
//        String phone = (String) value;
//        if (this.questionOne==null){
//            throw new ValidatorException(message);
//        }
//        else if (this.questionTwo==null){
//            throw new ValidatorException(message);
//        }
//    }
//    
//    public void sendSurveyResults(){
//        String from = "pirwebapp@gmail.com";
//        String password = "saints32";
//        String to = "dean7391@gmail.com";
//        String subject="Survey Response";
//        
//        String temp="";
//        for(int i=0;i<this.questionTwo.length;i++){
//            temp += this.questionTwo[i]+";";
//        }
//        
//        
//        String body = 
//                "<html>"
//                + "<center><body>"
//                +"<img width='104' height='107' src='http://justheardthat.com/sh.png'/>"
//                + "<h3>Thank you for registering!</h3>"
//                + "<table>"
//                    + "<tr>"
//                        + "<td>What category does your major fall under? </td>"
//                        + "<td>"+this.questionOne+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How did you hear about the program?></td>"
//                        + "<td>"+temp+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How many semesters have you participated in Partners in Reading? </td>"
//                        + "<td>"+this.questionThree+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>Why did you join Partners in Reading? </td>"
//                        + "<td>"+temp+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>Would you recommend Partners in Reading to someone else? </td>"
//                        + "<td>"+this.questionFive+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>Do you plan to participate in Partners in Reading next semester?</td>"
//                        + "<td>"+this.questionSix+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How would you summarize your experience?</td>"
//                        + "<td>"+this.questionSeven+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>Did you ever have any problems with your partner?</td>"
//                        + "<td>"+temp+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How well were the following goals for Partners in Reading met?</td>"
//                        +"<td>Feeling like you've made an impact</td>"
//                        + "<td>"+this.questionNine+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How well were the following goals for Partners in Reading met?</td>"
//                        +"<td>Finding books you like to read</td>"
//                        + "<td>"+this.questionTen+"</td>"
//                    + "</tr>"
//                     + "<tr>		"
//                        + "<td>How well were the following goals for Partners in Reading met?</td>"
//                        +"<td>Improving your ability to work with children</td>"
//                        + "<td>"+this.questionEleven+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How well were the following goals for Partners in Reading met?</td>"
//                        +"<td>Friendly atmosphere</td>"
//                        + "<td>"+this.questionTwelve+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How well were the following goals for Partners in Reading met?</td>"
//                        +"<td>Enjoyable sessions</td>"
//                        + "<td>"+this.questionThirteen+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How well were the following goals for Partners in Reading met?</td>"
//                        +"<td>Familiarizing yourself with the resources of the library</td>"
//                        + "<td>"+this.questionFourteen+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How organized do you feel the program was?</td>"
//                        + "<td>"+this.questionFifteen+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>How well do you feel your orientation prepared you for Partners in Reading?</td>"
//                        + "<td>"+this.questionSixteen+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>Did the tour during your orientation help you to find library resources during your Partners in Reading sessions? </td>"
//                        + "<td>"+this.questionSeventeen+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>Did you drive to Partners in Reading?</td>"
//                        + "<td>"+this.questionEighteen+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>Do you have any suggestions for how to make orientation better?</td>"
//                        + "<td>"+this.questionNineteen+"</td>"
//                    + "</tr>"
//                    + "<tr>		"
//                        + "<td>We really appreciate your input on Partners in Reading. Please let us know any thoughts that you have on the program.</td>"
//                        + "<td>"+this.questionTwenty+"</td>"
//                    + "</tr>"
//                + "</table>"
//            + "</body></center>"
//   + "</html>";
//                
//        SendMail email = new SendMail(from,to,subject,body, password);
//        email.send();
//        
//    }
}
