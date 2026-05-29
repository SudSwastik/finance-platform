package com.finance.platform.budget.web.dto;

import java.math.BigDecimal;

public record BudgetCategoryItemDto(
        String id,
        String name,
        String colorToken,
        String spent,
        String budget,
        BigDecimal percentUsed) {
}
