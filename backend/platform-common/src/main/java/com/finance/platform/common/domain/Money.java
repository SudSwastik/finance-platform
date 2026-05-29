package com.finance.platform.common.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable money value object. Never use float/double for currency.
 */
public final class Money {

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(String value) {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Money value cannot be blank");
        }
        return new Money(new BigDecimal(value.trim()));
    }

    public static Money of(BigDecimal value) {
        Objects.requireNonNull(value, "value");
        return new Money(value);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public BigDecimal amount() {
        return amount;
    }

    /** API / JSON representation per OpenAPI Money schema. */
    public String toApiString() {
        return amount.stripTrailingZeros().scale() <= 0
                ? amount.setScale(0, RoundingMode.UNNECESSARY).toPlainString()
                : amount.toPlainString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money money)) {
            return false;
        }
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return toApiString();
    }
}
