package com.sfuosdev.emailbackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sfuosdev.emailbackend.model.Application;
import com.sfuosdev.emailbackend.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);
    private final ObjectMapper objectMapper;
    private final Path dataDir;
    private final Path applicationsFile;
    private final Path teamsFile;
    
    public DataService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.dataDir = Paths.get("data");
        this.applicationsFile = dataDir.resolve("applications.json");
        this.teamsFile = dataDir.resolve("teams.json");
        initializeDataFiles();
    }
    
    private void initializeDataFiles() {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(dataDir);
            
            // Initialize applications file
            if (!Files.exists(applicationsFile)) {
                saveApplications(new ArrayList<>());
            }
            
            // Initialize teams file with default teams
            if (!Files.exists(teamsFile)) {
                List<Team> defaultTeams = createDefaultTeams();
                saveTeams(defaultTeams);
            }
            
            logger.info("✅ Data service initialized successfully");
        } catch (Exception e) {
            logger.error("❌ Failed to initialize data service: {}", e.getMessage());
        }
    }
    
    private List<Team> createDefaultTeams() {
        List<Team> teams = new ArrayList<>();
        
        Team engineering = new Team("Engineering", "Software development and technical roles");
        engineering.setId("engineering");
        engineering.getExecutives().add("cto@company.com");
        engineering.getProjectLeads().add("eng-lead@company.com");
        teams.add(engineering);
        
        Team product = new Team("Product", "Product management and design roles");
        product.setId("product");
        product.getExecutives().add("cpo@company.com");
        product.getProjectLeads().add("product-lead@company.com");
        teams.add(product);
        
        Team marketing = new Team("Marketing", "Marketing and growth roles");
        marketing.setId("marketing");
        marketing.getExecutives().add("cmo@company.com");
        marketing.getProjectLeads().add("marketing-lead@company.com");
        teams.add(marketing);
        
        return teams;
    }
    
    // Application methods
    public Application saveApplication(Application application) throws IOException {
        List<Application> applications = getAllApplications();
        applications.add(application);
        saveApplications(applications);
        return application;
    }
    
    public List<Application> getAllApplications() {
        try {
            if (!Files.exists(applicationsFile)) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(applicationsFile.toFile(), new TypeReference<List<Application>>() {});
        } catch (IOException e) {
            logger.error("Error reading applications: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Optional<Application> getApplicationById(String id) {
        return getAllApplications().stream()
                .filter(app -> app.getId().equals(id))
                .findFirst();
    }
    
    public Application updateApplicationStatus(String id, String status) throws IOException {
        List<Application> applications = getAllApplications();
        Optional<Application> applicationOpt = applications.stream()
                .filter(app -> app.getId().equals(id))
                .findFirst();
        
        if (applicationOpt.isEmpty()) {
            throw new IllegalArgumentException("Application not found");
        }
        
        Application application = applicationOpt.get();
        application.setStatus(status);
        saveApplications(applications);
        return application;
    }
    
    private void saveApplications(List<Application> applications) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(applicationsFile.toFile(), applications);
    }
    
    // Teams methods
    public Team saveTeam(Team team) throws IOException {
        List<Team> teams = getAllTeams();
        teams.removeIf(t -> t.getId().equals(team.getId()));
        teams.add(team);
        saveTeams(teams);
        return team;
    }
    
    public List<Team> getAllTeams() {
        try {
            if (!Files.exists(teamsFile)) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(teamsFile.toFile(), new TypeReference<List<Team>>() {});
        } catch (IOException e) {
            logger.error("Error reading teams: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Optional<Team> getTeamById(String id) {
        return getAllTeams().stream()
                .filter(team -> team.getId().equals(id))
                .findFirst();
    }
    
    public Optional<Team> getTeamByName(String name) {
        return getAllTeams().stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst();
    }
    
    public void deleteTeam(String id) throws IOException {
        List<Team> teams = getAllTeams();
        boolean removed = teams.removeIf(team -> team.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Team not found");
        }
        saveTeams(teams);
    }
    
    private void saveTeams(List<Team> teams) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(teamsFile.toFile(), teams);
    }
    
    // Statistics methods
    public List<Application> getRecentApplications(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        return getAllApplications().stream()
                .filter(app -> app.getAppliedAt().isAfter(cutoff))
                .toList();
    }
}