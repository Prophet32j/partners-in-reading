package com.rm.pir.utilities;

import java.util.ArrayList;
import java.util.List;

public class Mailer {
    
    private List<String> to;
    private String subject;
    private String body;
    
    public Mailer(List<String> to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
    
    public Mailer(String to, String subject, String body) {
        List<String> list = new ArrayList<>();
        list.add(to);
        
        this.to = list;
        this.subject = subject;
        this.body = body;
    }
    
    public void send() {
        // anonymous inner class to send out mail
        SendGridMailer sgm = new SendGridMailer();
        sgm.send(to, subject, body);
    }
}
