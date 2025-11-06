package com.ucfr.model;

import java.util.List;

public class CanonicalRecord {
    private List<AccountRecord> linkedRecords;
    private Double canonicalBalance;
    private String canonicalStatus;
    private double confidence;
    private String explain;

    // getters/setters
    public List<AccountRecord> getLinkedRecords() { return linkedRecords; }
    public void setLinkedRecords(List<AccountRecord> linkedRecords) { this.linkedRecords = linkedRecords; }
    public Double getCanonicalBalance() { return canonicalBalance; }
    public void setCanonicalBalance(Double canonicalBalance) { this.canonicalBalance = canonicalBalance; }
    public String getCanonicalStatus() { return canonicalStatus; }
    public void setCanonicalStatus(String canonicalStatus) { this.canonicalStatus = canonicalStatus; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public String getExplain() { return explain; }
    public void setExplain(String explain) { this.explain = explain; }
}
