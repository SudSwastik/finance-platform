package com.finance.platform.common.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void ofString_parsesAndScalesToTwoDecimals() {
        assertEquals("128.45", Money.of("128.45").toApiString());
        assertEquals("128.50", Money.of("128.5").toApiString());
    }

    @Test
    void add_sumsWithSameScale() {
        Money result = Money.of("10.00").add(Money.of("2.50"));
        assertEquals("12.50", result.toApiString());
    }

    @Test
    void of_blankThrows() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("  "));
    }

    @Test
    void equals_comparesNumericValue() {
        assertEquals(Money.of("1.0"), Money.of("1.00"));
    }
}
