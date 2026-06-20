package com.finance.platform.finance.web.dto;

public record RecentTransactionDto(
        String id,
        String merchantName,
        String category,
        String type,
        String amount,
        String transactionDate) {}
