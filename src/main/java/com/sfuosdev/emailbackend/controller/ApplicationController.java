package com.sfuosdev.emailbackend.controller;

import com.sfuosdev.emailbackend.dto.ApiResponse;
import com.sfuosdev.emailbackend.dto.ApplicationStats;
import com.sfuosdev.emailbackend.dto.ApplicationSubmissionResponse;
import com.sfuosdev.emailbackend.model.Application;
import com.sfuosdev.emailbackend.model.Team;
import com.sfuosdev.emailbackend.service.DataService;
import com.sfuosdev.emailbackend.service.EmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    
    private final DataService dataService;
    private final EmailService emailService;
    
    public ApplicationController(DataService dataService, EmailService emailService) {
        this.dataService = dataService;
        this.emailService = emailService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationSubmissionResponse>> submitApplication(@Valid @RequestBody Application application) {
        try {
            // Check if the team exists
            Optional<Team> team = dataService.getTeamByName(application.getTeam());
            if (team.isEmpty()) {
                List<String> availableTeams = dataService.getAllTeams().stream()
                        .map(Team::getName)
                        .collect(Collectors.toList());
                
                return ResponseEntity.badRequest().body(
                    ApiResponse.error("Invalid team", 
                        String.format("Team \"%s\" not found. Please use one of the existing teams: %s", 
                            application.getTeam(), String.join(", ", availableTeams)))
                );
            }
            
            // Save the application
            Application savedApplication = dataService.saveApplication(application);
            
            // Get notification recipients from the team
            List<String> recipients = team.get().getAllNotificationEmails();
            
            // Send email notifications
            EmailService.EmailResult emailResult = emailService.sendApplicationNotification(
                savedApplication, recipients).get();
            
            ApplicationSubmissionResponse.EmailNotificationResult notificationResult = 
                new ApplicationSubmissionResponse.EmailNotificationResult(
                    emailResult.isSuccess(), recipients, emailResult.getMessage());
            
            ApplicationSubmissionResponse response = new ApplicationSubmissionResponse(
                savedApplication, notificationResult);
            
            logger.info("📧 Application notification sent for {} ({}) to team {}", 
                application.getApplicantName(), application.getPosition(), team.get().getName());
            logger.info("Recipients: {}", String.join(", ", recipients));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Application submitted successfully", response)
            );
            
        } catch (IOException e) {
            logger.error("Error submitting application: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Failed to submit application", e.getMessage())
            );
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error sending email notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Failed to send email notification", e.getMessage())
            );
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllApplications(
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String status) {
        
        List<Application> applications = dataService.getAllApplications();
        
        // Apply filters
        if (team != null && !team.isEmpty()) {
            applications = applications.stream()
                    .filter(app -> app.getTeam().equalsIgnoreCase(team))
                    .collect(Collectors.toList());
        }
        
        if (status != null && !status.isEmpty()) {
            applications = applications.stream()
                    .filter(app -> app.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        
        Map<String, Object> result = Map.of(
            "count", applications.size(),
            "applications", applications
        );
        
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", result));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Application>> getApplicationById(@PathVariable String id) {
        Optional<Application> application = dataService.getApplicationById(id);
        
        if (application.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("Application not found", 
                    String.format("Application with ID \"%s\" does not exist", id))
            );
        }
        
        return ResponseEntity.ok(ApiResponse.success("Application retrieved successfully", application.get()));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Application>> updateApplicationStatus(
            @PathVariable String id, 
            @RequestBody Map<String, String> request) {
        
        String status = request.get("status");
        if (status == null || status.isEmpty()) {
            return ResponseEntity.badRequest().body(
                ApiResponse.error("Status is required", "Please provide a status value")
            );
        }
        
        List<String> validStatuses = Arrays.asList("pending", "reviewing", "interview", "accepted", "rejected");
        if (!validStatuses.contains(status)) {
            return ResponseEntity.badRequest().body(
                ApiResponse.error("Invalid status", 
                    String.format("Status must be one of: %s", String.join(", ", validStatuses)))
            );
        }
        
        try {
            Application updatedApplication = dataService.updateApplicationStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Application status updated successfully", updatedApplication));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("Application not found", e.getMessage())
            );
        } catch (IOException e) {
            logger.error("Error updating application status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Failed to update application status", e.getMessage())
            );
        }
    }
    
    @GetMapping("/stats/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getApplicationStats() {
        List<Application> applications = dataService.getAllApplications();
        
        // Count by status
        Map<String, Integer> byStatus = applications.stream()
                .collect(Collectors.groupingBy(
                    Application::getStatus,
                    Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
        
        // Count by team
        Map<String, Integer> byTeam = applications.stream()
                .collect(Collectors.groupingBy(
                    Application::getTeam,
                    Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
        
        // Recent applications (last 7 days)
        int recent = dataService.getRecentApplications(7).size();
        
        ApplicationStats stats = new ApplicationStats(applications.size(), byStatus, byTeam, recent);
        
        Map<String, Object> result = Map.of("stats", stats);
        return ResponseEntity.ok(ApiResponse.success("Application statistics retrieved successfully", result));
    }
}