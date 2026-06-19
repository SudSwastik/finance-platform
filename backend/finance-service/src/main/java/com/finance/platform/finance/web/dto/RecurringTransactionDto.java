package com.finance.platform.finance.web.dto;

public record RecurringTransactionDto(
        String name,
        String frequency,
        String amount,
        String nextDate) {}
