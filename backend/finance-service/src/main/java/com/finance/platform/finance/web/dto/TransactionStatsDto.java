package com.finance.platform.finance.web.dto;

public record TransactionStatsDto(
        String moneyIn,
        long moneyInCount,
        String moneyOut,
        long moneyOutCount,
        String netFlow,
        long totalCount
) {}
