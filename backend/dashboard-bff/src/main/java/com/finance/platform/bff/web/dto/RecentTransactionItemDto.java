package com.finance.platform.bff.web.dto;

public record RecentTransactionItemDto(
        String id,
        String merchantName,
        String category,
        String type,
        String amount,
        String transactionDate) {}
