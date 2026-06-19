package com.finance.platform.common.domain;

import java.time.Instant;

/** Plain value object for audit metadata. JPA mapping is inline per-service; no annotations here. */
public final class AuditMetadata {

    private final String entrySource;
    private final String createdBy;
    private final Instant createdAt;

    public AuditMetadata(String entrySource, String createdBy) {
        this.entrySource = entrySource;
        this.createdBy   = createdBy;
        this.createdAt   = Instant.now();
    }

    public String entrySource() { return entrySource; }
    public String createdBy()   { return createdBy; }
    public Instant createdAt()  { return createdAt; }
}
