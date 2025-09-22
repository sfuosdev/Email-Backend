package com.sfuosdev.emailbackend.controller;

import com.sfuosdev.emailbackend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SystemController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = Map.of(
            "status", "healthy",
            "timestamp", LocalDateTime.now(),
            "service", "Email Backend"
        );
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        Map<String, Object> endpoints = Map.of(
            "health", "/health",
            "applications", "/api/applications",
            "teams", "/api/teams"
        );
        
        Map<String, Object> info = Map.of(
            "message", "Executive Hiring Email Notification System",
            "version", "1.0.0",
            "endpoints", endpoints
        );
        
        return ResponseEntity.ok(info);
    }
}