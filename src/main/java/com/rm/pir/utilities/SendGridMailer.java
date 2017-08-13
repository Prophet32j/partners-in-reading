/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.pir.utilities;

import com.sendgrid.*;
import java.io.IOException;
import java.util.List;
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
        
        Personalization p = new Personalization();
        for (String r : recipients) {
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
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch(IOException ex) {
            Logger.getLogger(SendGridMailer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
}
