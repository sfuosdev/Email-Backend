package com.sfuosdev.emailbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    
    @Value("${spring.mail.host:#{null}}")
    private String host;
    
    @Value("${spring.mail.port:587}")
    private int port;
    
    @Value("${spring.mail.username:#{null}}")
    private String username;
    
    @Value("${spring.mail.password:#{null}}")
    private String password;
    
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        if (host != null && username != null && password != null) {
            mailSender.setHost(host);
            mailSender.setPort(port);
            mailSender.setUsername(username);
            mailSender.setPassword(password);
            
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "false");
        }
        
        return mailSender;
    }
}