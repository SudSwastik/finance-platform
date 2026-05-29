package com.finance.platform.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "platform.security")
public class PlatformSecurityProperties {

    /**
     * When true, accepts {@code X-Dev-User-Sub} for local development (never enable in production).
     */
    private boolean devUserSubEnabled = true;

    private String devUserSubHeader = "X-Dev-User-Sub";

    public boolean isDevUserSubEnabled() {
        return devUserSubEnabled;
    }

    public void setDevUserSubEnabled(boolean devUserSubEnabled) {
        this.devUserSubEnabled = devUserSubEnabled;
    }

    public String getDevUserSubHeader() {
        return devUserSubHeader;
    }

    public void setDevUserSubHeader(String devUserSubHeader) {
        this.devUserSubHeader = devUserSubHeader;
    }
}
