package com.finance.platform.goals.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "goal_contributions", schema = "goals")
public class GoalContributionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "goal_id", nullable = false)
    private UUID goalId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_sub", nullable = false)
    private String userSub;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column
    private String note;

    @Column(name = "contributed_at", nullable = false)
    private Instant contributedAt;

    protected GoalContributionJpaEntity() {
    }

    public GoalContributionJpaEntity(UUID id, UUID goalId, UUID tenantId, String userSub,
                                      BigDecimal amount, String note, Instant contributedAt) {
        this.id = id;
        this.goalId = goalId;
        this.tenantId = tenantId;
        this.userSub = userSub;
        this.amount = amount;
        this.note = note;
        this.contributedAt = contributedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getGoalId() {
        return goalId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getUserSub() {
        return userSub;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public Instant getContributedAt() {
        return contributedAt;
    }
}
