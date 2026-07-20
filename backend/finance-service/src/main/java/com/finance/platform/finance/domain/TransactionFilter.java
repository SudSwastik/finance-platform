package com.finance.platform.finance.domain;

import java.time.YearMonth;
import java.util.UUID;

public record TransactionFilter(
        String typeGroup,  // "INCOME" | "EXPENSE" | "TRANSFERS" | null = All
        String search,
        YearMonth month,
        UUID accountId,
        String category,
        String status,
        boolean recurringOnly,
        RecurringFrequency recurringFrequency
) {
    public static TransactionFilter empty() {
        return new TransactionFilter(null, null, null, null, null, null, false, null);
    }
}
