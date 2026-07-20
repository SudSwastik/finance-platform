package com.finance.platform.finance.web.dto;

import java.util.UUID;

public record AccountDetailDto(
        UUID id,
        String type,
        String name,
        String currency,
        String balance,
        String monthChange,
        String moneyInMonth,
        String moneyOutMonth,
        String avgDailyMonth
) {}
