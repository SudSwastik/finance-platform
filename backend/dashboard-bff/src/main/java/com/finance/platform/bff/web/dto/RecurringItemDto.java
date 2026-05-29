package com.finance.platform.bff.web.dto;

import java.time.LocalDate;

public record RecurringItemDto(
        String id,
        String name,
        String frequency,
        String amount,
        LocalDate nextDate) {
}
