package com.finance.platform.budget.domain;

import com.finance.platform.common.domain.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public final class BudgetCategory {

    private final UUID id;
    private final String name;
    private final String colorToken;
    private final Money spent;
    private final Money budgetCap;

    public BudgetCategory(UUID id, String name, String colorToken, Money spent, Money budgetCap) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.colorToken = Objects.requireNonNull(colorToken);
        this.spent = Objects.requireNonNull(spent);
        this.budgetCap = Objects.requireNonNull(budgetCap);
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String colorToken() {
        return colorToken;
    }

    public Money spent() {
        return spent;
    }

    public Money budgetCap() {
        return budgetCap;
    }

    public BigDecimal percentUsed() {
        if (budgetCap.amount().signum() == 0) {
            return BigDecimal.ZERO;
        }
        return spent.amount()
                .multiply(BigDecimal.valueOf(100))
                .divide(budgetCap.amount(), 1, RoundingMode.HALF_UP);
    }
}
