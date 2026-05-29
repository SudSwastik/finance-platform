package com.finance.platform.budget.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "user_budget_summary", schema = "budget")
public class UserBudgetSummaryJpaEntity {

    @Id
    @Column(name = "user_sub")
    private String userSub;

    @Column(name = "total_display", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalDisplay;

    protected UserBudgetSummaryJpaEntity() {
    }

    public String getUserSub() {
        return userSub;
    }

    public BigDecimal getTotalDisplay() {
        return totalDisplay;
    }
}
