/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.utilities;

import com.sendgrid.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joshuahardy
 */
public class SendGridMailer {
    
    public void send(List<String> recipients, String subject, String body) {
        
        Email from = new Email(Constants.ADMIN_NPL_EMAIL);
        
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        
        Personalization p = new Personalization();
        // dedupe the list because it will error out the API call
        for (String r : dedupe(recipients)) {
            p.addTo(new Email(r));
        }
        Content content = new Content("text/html", body);
        
        mail.addPersonalization(p);
        mail.addContent(content);
        
        SendGrid sg = new SendGrid(Constants.SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch(IOException ex) {
            Logger logger = Logger.getLogger(SendGridMailer.class.getName());
            logger.log(Level.WARNING, "Failed to send email: " + subject);
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    private Set<String> dedupe(List<String> recipients) {
        return new HashSet<>(recipients);
    }
    
}
