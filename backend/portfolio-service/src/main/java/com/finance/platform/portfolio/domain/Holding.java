package com.finance.platform.portfolio.domain;

import com.finance.platform.common.domain.Money;

import java.math.BigDecimal;
import java.util.UUID;

public record Holding(
        UUID id,
        String symbol,
        String name,
        Money costBasis,
        BigDecimal changePercent,
        Money currentValue
) {}
