package com.finance.platform.budget.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "budget_categories", schema = "budget")
public class BudgetCategoryJpaEntity {

    @Id
    private UUID id;

    @Column(name = "user_sub", nullable = false)
    private String userSub;

    @Column(nullable = false)
    private String name;

    @Column(name = "color_token", nullable = false)
    private String colorToken;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal spent;

    @Column(name = "budget_cap", nullable = false, precision = 19, scale = 2)
    private BigDecimal budgetCap;

    protected BudgetCategoryJpaEntity() {
    }

    public UUID getId() {
        return id;
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

    public BigDecimal getSpent() {
        return spent;
    }

    public BigDecimal getBudgetCap() {
        return budgetCap;
    }
}
