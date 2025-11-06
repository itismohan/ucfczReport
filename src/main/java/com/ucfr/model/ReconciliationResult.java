package com.ucfr.model;

import java.util.List;

public class ReconciliationResult {
    private String consumerToken;
    private String timestamp;
    private List<CanonicalRecord> canonicalRecords;

    // getters/setters
    public String getConsumerToken() { return consumerToken; }
    public void setConsumerToken(String consumerToken) { this.consumerToken = consumerToken; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public List<CanonicalRecord> getCanonicalRecords() { return canonicalRecords; }
    public void setCanonicalRecords(List<CanonicalRecord> canonicalRecords) { this.canonicalRecords = canonicalRecords; }

    @Override
    public String toString() {
        return "ReconciliationResult{" + "consumerToken='" + consumerToken + '\'' + ", timestamp='" + timestamp + '\'' + '}';
    }
}
