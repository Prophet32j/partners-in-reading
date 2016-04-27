package com.rm.pir.utilities;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private ScheduledExecutorService scheduler;
    
    private String to;
    private String subject;
    private String body;

    public MailSender(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }
    
    public void send() {
        // anonymous inner class to send out mail
        final Runnable mailer = new Runnable() {

            @Override
            public void run() {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
//                props.put("mail.smtp.starttls.enable", "true");
                props.setProperty("mail.smtp.host", "smtp.sendgrid.net");
                props.setProperty("mail.smtp.port", "587");
//                props.setProperty("mail.smtp.ssl.trust", "smtp.mandrillapp.com");
                props.setProperty("mail.smtp.user", Constants.ADMIN_SENDGRID_EMAIL);
                props.setProperty("mail.transport.protocol", "smtp");
                
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        Constants.ADMIN_SENDGRID_EMAIL, 
                                        Constants.ADMIN_SENDGRID_EMAIL_PASS);
                            }
                        });
//                        session.setDebug(true);
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(Constants.ADMIN_NPL_EMAIL));
                    message.setSubject(subject);
                    message.setContent(body, "text/html; charset=utf-8");
                    
                    InternetAddress[] addresses = InternetAddress.parse(to);
                    
                    // check to see how many addresses we have, might have to break up
                    // and send more emails
                    int maxSize = 200;
                    if (addresses.length > maxSize) {
                        int i=0;
                        while (i<addresses.length) {
                            // check the size we have left, adjust size of array as needed
                            int size = addresses.length - i >= maxSize ? maxSize : addresses.length - i;
                            InternetAddress[] temp = new InternetAddress[size];
                            for (int k=0; k<temp.length; k++) {
                                temp[k] = addresses[i++];
                            }
                            message.setRecipients(Message.RecipientType.TO, temp);
                            Transport.send(message);
                        }
                    } else {
                        message.setRecipients(Message.RecipientType.TO, addresses);
                        Transport.send(message);
                    }
                } catch (MessagingException ex) {
                    Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
                    Logger.getLogger(MailSender.class.getName()).log(new LogRecord(Level.SEVERE, "Message Send Error"));
                    throw new RuntimeException(ex);
                }
            }
        };
        // schedule the mailer to send the mail
        try {
        scheduler.schedule(mailer, 0, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
