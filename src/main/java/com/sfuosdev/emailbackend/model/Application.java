package com.sfuosdev.emailbackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class Application {
    private String id;
    
    @NotBlank(message = "Applicant name is required")
    private String applicantName;
    
    @NotBlank(message = "Applicant email is required")
    @Email(message = "Invalid email format")
    private String applicantEmail;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    @NotBlank(message = "Team is required")
    private String team;
    
    private String resumeUrl;
    private String coverLetter;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appliedAt;
    
    private String status = "pending";
    
    public Application() {
        this.id = generateId();
        this.appliedAt = LocalDateTime.now();
    }
    
    public Application(String applicantName, String applicantEmail, String position, String team) {
        this();
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.position = position;
        this.team = team;
    }
    
    private String generateId() {
        return Long.toString(System.currentTimeMillis(), 36) + 
               UUID.randomUUID().toString().substring(0, 8);
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getApplicantName() {
        return applicantName;
    }
    
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
    
    public String getApplicantEmail() {
        return applicantEmail;
    }
    
    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getTeam() {
        return team;
    }
    
    public void setTeam(String team) {
        this.team = team;
    }
    
    public String getResumeUrl() {
        return resumeUrl;
    }
    
    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    
    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
    
    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}