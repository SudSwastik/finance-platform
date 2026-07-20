package com.finance.platform.finance.web.dto;

public record SubscriptionDto(
        String id,
        String name,
        String category,
        String amount,
        String currency,
        String frequency,
        String nextDueDate,
        String status,
        String accountName
) {}
