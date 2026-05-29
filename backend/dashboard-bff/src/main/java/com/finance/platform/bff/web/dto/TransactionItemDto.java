package com.finance.platform.bff.web.dto;

import java.time.LocalDate;

public record TransactionItemDto(
        String id,
        String merchant,
        String category,
        String amount,
        LocalDate date) {
}
