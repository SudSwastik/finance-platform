package com.finance.platform.goals.domain;

import com.finance.platform.common.domain.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class Goal {

    private final UUID id;
    private final String userSub;
    private final String name;
    private final String colorToken;
    private final Money current;
    private final Money target;
    private final LocalDate targetDate;

    public Goal(UUID id, String userSub, String name, String colorToken, Money current, Money target, LocalDate targetDate) {
        this.id = Objects.requireNonNull(id);
        this.userSub = Objects.requireNonNull(userSub);
        this.name = Objects.requireNonNull(name);
        this.colorToken = Objects.requireNonNull(colorToken);
        this.current = Objects.requireNonNull(current);
        this.target = Objects.requireNonNull(target);
        this.targetDate = Objects.requireNonNull(targetDate);
    }

    public UUID id() {
        return id;
    }

    public String userSub() {
        return userSub;
    }

    public String name() {
        return name;
    }

    public String colorToken() {
        return colorToken;
    }

    public Money current() {
        return current;
    }

    public Money target() {
        return target;
    }

    public LocalDate targetDate() {
        return targetDate;
    }

    public BigDecimal percent() {
        if (target.amount().signum() == 0) {
            return BigDecimal.ZERO;
        }
        return current.amount()
                .multiply(BigDecimal.valueOf(100))
                .divide(target.amount(), 1, RoundingMode.HALF_UP);
    }

    public Goal withContribution(Money amount) {
        return new Goal(id, userSub, name, colorToken, current.add(amount), target, targetDate);
    }

    public Goal withName(String newName) {
        return new Goal(id, userSub, newName, colorToken, current, target, targetDate);
    }

    public Goal withTarget(Money newTarget) {
        return new Goal(id, userSub, name, colorToken, current, newTarget, targetDate);
    }

    public Goal withTargetDate(LocalDate newTargetDate) {
        return new Goal(id, userSub, name, colorToken, current, target, newTargetDate);
    }
}
