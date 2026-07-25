package com.finance.platform.goals.web.dto;

import java.math.BigDecimal;

public record GoalItemDto(
        String id,
        String name,
        String current,
        String target,
        BigDecimal percent,
        String colorToken,
        String targetDate) {
}
