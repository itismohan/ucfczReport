package com.ucfr.model;

import java.util.List;

public class Report {
    private String source; // Experian/Equifax/TransUnion
    private List<AccountRecord> accounts;

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public List<AccountRecord> getAccounts() { return accounts; }
    public void setAccounts(List<AccountRecord> accounts) { this.accounts = accounts; }
}
