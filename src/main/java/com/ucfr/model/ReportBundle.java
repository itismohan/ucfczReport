package com.ucfr.model;

import java.util.List;

public class ReportBundle {
    private String consumerToken;
    private List<Report> reports;

    // getters/setters
    public String getConsumerToken() { return consumerToken; }
    public void setConsumerToken(String consumerToken) { this.consumerToken = consumerToken; }
    public List<Report> getReports() { return reports; }
    public void setReports(List<Report> reports) { this.reports = reports; }
}
