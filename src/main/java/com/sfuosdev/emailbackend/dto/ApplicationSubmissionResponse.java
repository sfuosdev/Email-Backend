package com.sfuosdev.emailbackend.dto;

import com.sfuosdev.emailbackend.model.Application;

import java.util.List;

public class ApplicationSubmissionResponse {
    private Application application;
    private EmailNotificationResult notification;
    
    public ApplicationSubmissionResponse() {}
    
    public ApplicationSubmissionResponse(Application application, EmailNotificationResult notification) {
        this.application = application;
        this.notification = notification;
    }
    
    public Application getApplication() {
        return application;
    }
    
    public void setApplication(Application application) {
        this.application = application;
    }
    
    public EmailNotificationResult getNotification() {
        return notification;
    }
    
    public void setNotification(EmailNotificationResult notification) {
        this.notification = notification;
    }
    
    public static class EmailNotificationResult {
        private boolean sent;
        private List<String> recipients;
        private String details;
        
        public EmailNotificationResult() {}
        
        public EmailNotificationResult(boolean sent, List<String> recipients, String details) {
            this.sent = sent;
            this.recipients = recipients;
            this.details = details;
        }
        
        public boolean isSent() {
            return sent;
        }
        
        public void setSent(boolean sent) {
            this.sent = sent;
        }
        
        public List<String> getRecipients() {
            return recipients;
        }
        
        public void setRecipients(List<String> recipients) {
            this.recipients = recipients;
        }
        
        public String getDetails() {
            return details;
        }
        
        public void setDetails(String details) {
            this.details = details;
        }
    }
}