package com.sfuosdev.emailbackend.service;

import com.sfuosdev.emailbackend.model.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username:#{null}}")
    private String fromEmail;
    
    @Value("${app.name:Executive Hiring Notification System}")
    private String appName;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Async
    public CompletableFuture<EmailResult> sendApplicationNotification(Application application, List<String> recipients) {
        try {
            if (fromEmail == null || fromEmail.isEmpty()) {
                // Log email instead of sending it
                logEmailNotification(application, recipients);
                return CompletableFuture.completedFuture(
                    new EmailResult(true, "Email logged (SMTP not configured)", "mock-" + System.currentTimeMillis())
                );
            }
            
            String subject = String.format("New Application: %s - %s", 
                application.getPosition(), application.getApplicantName());
            
            String htmlContent = generateApplicationNotificationHTML(application);
            String textContent = generateApplicationNotificationText(application);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(recipients.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(textContent, htmlContent);
            
            mailSender.send(message);
            
            logger.info("✅ Email sent successfully to: {}", String.join(", ", recipients));
            return CompletableFuture.completedFuture(
                new EmailResult(true, "Email sent successfully", message.getMessageID())
            );
            
        } catch (MessagingException e) {
            logger.error("❌ Failed to send email: {}", e.getMessage());
            return CompletableFuture.completedFuture(
                new EmailResult(false, e.getMessage(), null)
            );
        }
    }
    
    private void logEmailNotification(Application application, List<String> recipients) {
        String subject = String.format("New Application: %s - %s", 
            application.getPosition(), application.getApplicantName());
        String textContent = generateApplicationNotificationText(application);
        
        logger.info("📧 EMAIL NOTIFICATION (not sent - no SMTP configured):");
        logger.info("To: {}", String.join(", ", recipients));
        logger.info("Subject: {}", subject);
        logger.info("Content: {}", textContent);
        logger.info("---");
        logger.info("📧 Application notification sent for {} ({}) to team {}", 
            application.getApplicantName(), application.getPosition(), application.getTeam());
        logger.info("Recipients: {}", String.join(", ", recipients));
    }
    
    private String generateApplicationNotificationHTML(Application application) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm:ss a");
        String formattedDate = application.getAppliedAt().format(formatter);
        
        StringBuilder html = new StringBuilder();
        html.append("<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;\">");
        html.append("<h2 style=\"color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px;\">");
        html.append("New Job Application Received</h2>");
        
        html.append("<div style=\"background-color: #f8f9fa; padding: 20px; border-radius: 5px; margin: 20px 0;\">");
        html.append("<h3 style=\"color: #007bff; margin-top: 0;\">Applicant Information</h3>");
        html.append("<p><strong>Name:</strong> ").append(application.getApplicantName()).append("</p>");
        html.append("<p><strong>Email:</strong> <a href=\"mailto:").append(application.getApplicantEmail()).append("\">")
                .append(application.getApplicantEmail()).append("</a></p>");
        html.append("<p><strong>Position:</strong> ").append(application.getPosition()).append("</p>");
        html.append("<p><strong>Team:</strong> ").append(application.getTeam()).append("</p>");
        html.append("<p><strong>Applied At:</strong> ").append(formattedDate).append("</p>");
        html.append("</div>");
        
        if (application.getCoverLetter() != null && !application.getCoverLetter().isEmpty()) {
            html.append("<div style=\"background-color: #fff; padding: 20px; border-left: 4px solid #007bff; margin: 20px 0;\">");
            html.append("<h3 style=\"color: #333; margin-top: 0;\">Cover Letter</h3>");
            html.append("<p style=\"white-space: pre-wrap;\">").append(application.getCoverLetter()).append("</p>");
            html.append("</div>");
        }
        
        if (application.getResumeUrl() != null && !application.getResumeUrl().isEmpty()) {
            html.append("<div style=\"margin: 20px 0;\">");
            html.append("<h3 style=\"color: #333;\">Resume</h3>");
            html.append("<p><a href=\"").append(application.getResumeUrl()).append("\" style=\"color: #007bff; text-decoration: none;\">")
                    .append("📄 View Resume</a></p>");
            html.append("</div>");
        }
        
        html.append("<div style=\"background-color: #e9ecef; padding: 15px; border-radius: 5px; margin-top: 30px; text-align: center;\">");
        html.append("<p style=\"margin: 0; color: #6c757d; font-size: 12px;\">");
        html.append("This is an automated notification from the ").append(appName).append("</p>");
        html.append("</div>");
        html.append("</div>");
        
        return html.toString();
    }
    
    private String generateApplicationNotificationText(Application application) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm:ss a");
        String formattedDate = application.getAppliedAt().format(formatter);
        
        StringBuilder text = new StringBuilder();
        text.append("New Job Application Received\n\n");
        text.append("Applicant Information:\n");
        text.append("Name: ").append(application.getApplicantName()).append("\n");
        text.append("Email: ").append(application.getApplicantEmail()).append("\n");
        text.append("Position: ").append(application.getPosition()).append("\n");
        text.append("Team: ").append(application.getTeam()).append("\n");
        text.append("Applied At: ").append(formattedDate).append("\n\n");
        
        if (application.getCoverLetter() != null && !application.getCoverLetter().isEmpty()) {
            text.append("Cover Letter:\n").append(application.getCoverLetter()).append("\n\n");
        }
        
        if (application.getResumeUrl() != null && !application.getResumeUrl().isEmpty()) {
            text.append("Resume: ").append(application.getResumeUrl()).append("\n\n");
        }
        
        text.append("---\nThis is an automated notification from the ").append(appName);
        
        return text.toString();
    }
    
    public static class EmailResult {
        private final boolean success;
        private final String message;
        private final String messageId;
        
        public EmailResult(boolean success, String message, String messageId) {
            this.success = success;
            this.message = message;
            this.messageId = messageId;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getMessageId() {
            return messageId;
        }
    }
}