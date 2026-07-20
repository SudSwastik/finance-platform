package com.finance.platform.finance.application;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionStats(
        long activeCount,
        BigDecimal monthlyCost,
        BigDecimal yearlyCost,
        LocalDate nextRenewal
) {}
