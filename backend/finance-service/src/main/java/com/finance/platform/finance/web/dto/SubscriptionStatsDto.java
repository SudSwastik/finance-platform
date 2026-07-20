package com.finance.platform.finance.web.dto;

public record SubscriptionStatsDto(
        long activeCount,
        String monthlyCost,
        String yearlyCost,
        String nextRenewal
) {}
