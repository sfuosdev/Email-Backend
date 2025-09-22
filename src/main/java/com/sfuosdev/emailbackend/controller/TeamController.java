package com.sfuosdev.emailbackend.controller;

import com.sfuosdev.emailbackend.dto.ApiResponse;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
    
    private final DataService dataService;
    private final EmailService emailService;
    
    public TeamController(DataService dataService, EmailService emailService) {
        this.dataService = dataService;
        this.emailService = emailService;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllTeams() {
        List<Team> teams = dataService.getAllTeams();
        
        Map<String, Object> result = Map.of(
            "count", teams.size(),
            "teams", teams
        );
        
        return ResponseEntity.ok(ApiResponse.success("Teams retrieved successfully", result));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Team>> getTeamById(@PathVariable String id) {
        Optional<Team> team = dataService.getTeamById(id);
        
        if (team.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("Team not found", 
                    String.format("Team with ID \"%s\" does not exist", id))
            );
        }
        
        return ResponseEntity.ok(ApiResponse.success("Team retrieved successfully", team.get()));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Team>> createTeam(@Valid @RequestBody Team team) {
        try {
            // Check if a team with the same name already exists
            Optional<Team> existingTeam = dataService.getTeamByName(team.getName());
            if (existingTeam.isPresent()) {
                return ResponseEntity.badRequest().body(
                    ApiResponse.error("Team already exists", 
                        String.format("A team with the name \"%s\" already exists", team.getName()))
                );
            }
            
            // Validate that there's at least one email address
            if ((team.getExecutives() == null || team.getExecutives().isEmpty()) && 
                (team.getProjectLeads() == null || team.getProjectLeads().isEmpty())) {
                return ResponseEntity.badRequest().body(
                    ApiResponse.error("At least one executive or project lead email is required")
                );
            }
            
            Team savedTeam = dataService.saveTeam(team);
            
            logger.info("✅ New team created: {}", team.getName());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Team created successfully", savedTeam)
            );
            
        } catch (IOException e) {
            logger.error("Error creating team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Failed to create team", e.getMessage())
            );
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Team>> updateTeam(@PathVariable String id, @Valid @RequestBody Team team) {
        try {
            // Check if team exists
            Optional<Team> existingTeam = dataService.getTeamById(id);
            if (existingTeam.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error("Team not found", 
                        String.format("Team with ID \"%s\" does not exist", id))
                );
            }
            
            // Check if another team with the same name exists (excluding current team)
            Optional<Team> teamWithSameName = dataService.getTeamByName(team.getName());
            if (teamWithSameName.isPresent() && !teamWithSameName.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body(
                    ApiResponse.error("Team name already exists", 
                        String.format("Another team with the name \"%s\" already exists", team.getName()))
                );
            }
            
            // Validate that there's at least one email address
            if ((team.getExecutives() == null || team.getExecutives().isEmpty()) && 
                (team.getProjectLeads() == null || team.getProjectLeads().isEmpty())) {
                return ResponseEntity.badRequest().body(
                    ApiResponse.error("At least one executive or project lead email is required")
                );
            }
            
            // Preserve the original ID and creation date
            team.setId(id);
            team.setCreatedAt(existingTeam.get().getCreatedAt());
            
            Team savedTeam = dataService.saveTeam(team);
            
            logger.info("✅ Team updated: {}", team.getName());
            
            return ResponseEntity.ok(ApiResponse.success("Team updated successfully", savedTeam));
            
        } catch (IOException e) {
            logger.error("Error updating team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Failed to update team", e.getMessage())
            );
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable String id) {
        try {
            // Check if team exists
            Optional<Team> existingTeam = dataService.getTeamById(id);
            if (existingTeam.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error("Team not found", 
                        String.format("Team with ID \"%s\" does not exist", id))
                );
            }
            
            dataService.deleteTeam(id);
            
            logger.info("🗑️ Team deleted: {}", existingTeam.get().getName());
            
            return ResponseEntity.ok(ApiResponse.success("Team deleted successfully"));
            
        } catch (IOException e) {
            logger.error("Error deleting team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Failed to delete team", e.getMessage())
            );
        }
    }
    
    @PostMapping("/{id}/test-email")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testEmailNotification(@PathVariable String id) {
        try {
            Optional<Team> team = dataService.getTeamById(id);
            
            if (team.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error("Team not found", 
                        String.format("Team with ID \"%s\" does not exist", id))
                );
            }
            
            // Create a test application for email testing
            Application testApplication = new Application(
                "Test Applicant", 
                "test@example.com", 
                "Test Position", 
                team.get().getName()
            );
            testApplication.setCoverLetter("This is a test email notification to verify the email system is working correctly.");
            testApplication.setResumeUrl("https://example.com/test-resume.pdf");
            
            // Get notification recipients
            List<String> recipients = team.get().getAllNotificationEmails();
            
            // Send test email
            EmailService.EmailResult emailResult = emailService.sendApplicationNotification(
                testApplication, recipients).get();
            
            logger.info("📧 Test email sent for team {}", team.get().getName());
            
            Map<String, Object> result = Map.of(
                "recipients", recipients,
                "emailResult", Map.of(
                    "success", emailResult.isSuccess(),
                    "message", emailResult.getMessage(),
                    "messageId", emailResult.getMessageId() != null ? emailResult.getMessageId() : ""
                )
            );
            
            return ResponseEntity.ok(ApiResponse.success("Test email sent successfully", result));
            
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error sending test email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Failed to send test email", e.getMessage())
            );
        }
    }
    
    @GetMapping("/email/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkEmailServiceStatus() {
        // For now, we'll just return a simple status
        // In a more advanced implementation, we could test the email connection
        Map<String, Object> emailService = Map.of(
            "configured", this.emailService != null,
            "status", "available"
        );
        
        Map<String, Object> result = Map.of("emailService", emailService);
        return ResponseEntity.ok(ApiResponse.success("Email service status retrieved successfully", result));
    }
}