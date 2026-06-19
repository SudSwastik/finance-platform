package com.finance.platform.bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "platform.services")
public class PlatformServicesProperties {

    private String budget    = "http://localhost:8081";
    private String identity  = "http://localhost:8079";
    private String finance   = "http://localhost:8084";
    private String portfolio = "http://localhost:8085";

    public String getBudget()    { return budget; }
    public void setBudget(String budget) { this.budget = budget; }
    public String getIdentity()  { return identity; }
    public void setIdentity(String identity) { this.identity = identity; }
    public String getFinance()   { return finance; }
    public void setFinance(String finance) { this.finance = finance; }
    public String getPortfolio() { return portfolio; }
    public void setPortfolio(String portfolio) { this.portfolio = portfolio; }
}
