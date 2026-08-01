package com.finance.platform.goals.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "goals", schema = "goals")
public class GoalJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_sub", nullable = false)
    private String userSub;

    @Column(nullable = false)
    private String name;

    @Column(name = "color_token", nullable = false)
    private String colorToken;

    @Column(name = "current_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal current;

    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal target;

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Version
    @Column(nullable = false)
    private long version;

    protected GoalJpaEntity() {
    }

    public GoalJpaEntity(UUID id, UUID tenantId, String userSub, String name, String colorToken,
                          BigDecimal current, BigDecimal target, LocalDate targetDate, long version) {
        this.id = id;
        this.tenantId = tenantId;
        this.userSub = userSub;
        this.name = name;
        this.colorToken = colorToken;
        this.current = current;
        this.target = target;
        this.targetDate = targetDate;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getUserSub() {
        return userSub;
    }

    public String getName() {
        return name;
    }

    public String getColorToken() {
        return colorToken;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public long getVersion() {
        return version;
    }
}
