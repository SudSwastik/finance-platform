package com.finance.platform.common.domain;

import java.util.Objects;

/**
 * Cognito {@code sub} or dev seed user identifier.
 */
public record UserId(String value) {

    public UserId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be blank");
        }
    }

    public static UserId of(String value) {
        return new UserId(value);
    }
}
