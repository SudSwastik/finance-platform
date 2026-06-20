package com.finance.platform.finance.web.dto;

public record TransactionDto(
        String id,
        String merchantName,
        String category,
        String type,
        String status,
        String amount,
        String transactionDate,
        String accountName
) {}
