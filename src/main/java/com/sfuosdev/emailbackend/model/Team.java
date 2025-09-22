package com.sfuosdev.emailbackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private String id;
    
    @NotBlank(message = "Team name is required")
    private String name;
    
    private String description;
    
    @NotEmpty(message = "At least one executive or project lead email is required")
    private List<String> executives = new ArrayList<>();
    
    private List<String> projectLeads = new ArrayList<>();
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    public Team() {
        this.id = generateId();
        this.createdAt = LocalDateTime.now();
    }
    
    public Team(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
    
    private String generateId() {
        return Long.toString(System.currentTimeMillis(), 36) + 
               UUID.randomUUID().toString().substring(0, 8);
    }
    
    public List<String> getAllNotificationEmails() {
        List<String> allEmails = new ArrayList<>();
        allEmails.addAll(executives);
        allEmails.addAll(projectLeads);
        return allEmails;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<String> getExecutives() {
        return executives;
    }
    
    public void setExecutives(List<String> executives) {
        this.executives = executives != null ? executives : new ArrayList<>();
    }
    
    public List<String> getProjectLeads() {
        return projectLeads;
    }
    
    public void setProjectLeads(List<String> projectLeads) {
        this.projectLeads = projectLeads != null ? projectLeads : new ArrayList<>();
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}