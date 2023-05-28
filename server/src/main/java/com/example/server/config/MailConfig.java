package com.example.server.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import jakarta.mail.Session;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String SPRING_MAIL_HOST;

    @Value("${spring.mail.port}")
    private int SPRING_MAIL_PORT;

    @Value("${spring.mail.username}")
    private String SPRING_MAIL_USERNAME;

    @Value("${spring.mail.password}")
    private String SPRING_MAIL_PASSWORD;
    
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(SPRING_MAIL_HOST);
        mailSender.setPort(SPRING_MAIL_PORT);
        mailSender.setUsername(SPRING_MAIL_USERNAME);
        mailSender.setPassword(SPRING_MAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.starttls.required", true);
        // mailSender.setSession(Session.getDefaultInstance(System.getProperties(), null));

        return mailSender;
    }
}
