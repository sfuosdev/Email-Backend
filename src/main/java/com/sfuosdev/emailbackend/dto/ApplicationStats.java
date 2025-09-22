package com.sfuosdev.emailbackend.dto;

import java.util.Map;

public class ApplicationStats {
    private int total;
    private Map<String, Integer> byStatus;
    private Map<String, Integer> byTeam;
    private int recent;
    
    public ApplicationStats() {}
    
    public ApplicationStats(int total, Map<String, Integer> byStatus, Map<String, Integer> byTeam, int recent) {
        this.total = total;
        this.byStatus = byStatus;
        this.byTeam = byTeam;
        this.recent = recent;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    public Map<String, Integer> getByStatus() {
        return byStatus;
    }
    
    public void setByStatus(Map<String, Integer> byStatus) {
        this.byStatus = byStatus;
    }
    
    public Map<String, Integer> getByTeam() {
        return byTeam;
    }
    
    public void setByTeam(Map<String, Integer> byTeam) {
        this.byTeam = byTeam;
    }
    
    public int getRecent() {
        return recent;
    }
    
    public void setRecent(int recent) {
        this.recent = recent;
    }
}