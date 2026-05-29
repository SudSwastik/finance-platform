package com.finance.platform.bff.client.dto;

import java.math.BigDecimal;
import java.util.List;

public record TotalBudgetsSectionDto(String total, String filter, List<BudgetCategoryItemDto> categories) {

    public record BudgetCategoryItemDto(
            String id,
            String name,
            String colorToken,
            String spent,
            String budget,
            BigDecimal percentUsed) {
    }
}
