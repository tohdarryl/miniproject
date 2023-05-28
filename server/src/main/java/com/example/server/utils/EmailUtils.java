package com.example.server.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailUtils(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void forgotMail(String to, String subject, String password) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("thgdarryl@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        // before ng build
        // String htmlMsg = "<p><b>Your Login details for Beento App</b><br><b>Email: </b> " + to + " <br><b>Password resetted to: </b> " + password + "<br><a href=\"http://localhost:4200\">Click here to login</a></p>";
        // after ng build and saved into server's static folder
        // String htmlMsg = "<p><b>Your Login details for Beento App</b><br><b>Email: </b> " + to + " <br><b>Password resetted to: </b> " + password + "<br><a href=\"http://localhost:8080/login\">Click here to login</a></p>";
        // After deploying on railway, using generated domain
        String htmlMsg = "<p><b>Your Login details for Beento App</b><br><b>Email: </b> " + to + " <br><b>Password resetted to: </b> " + password + "<br><a href=\"http://beento-production.up.railway.app/login\">Click here to login</a></p>";
        message.setContent(htmlMsg, "text/html");
        mailSender.send(message);
    }
}
