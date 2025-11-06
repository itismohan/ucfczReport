package com.ucfr.model;

public class AccountRecord {
    private String accountId;
    private String accountName;
    private String issuer;
    private String type;
    private String status;
    private Double balance;
    private String lastReportDate;
    private String source; // populated during ingestion

    // getters/setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public String getLastReportDate() { return lastReportDate; }
    public void setLastReportDate(String lastReportDate) { this.lastReportDate = lastReportDate; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
