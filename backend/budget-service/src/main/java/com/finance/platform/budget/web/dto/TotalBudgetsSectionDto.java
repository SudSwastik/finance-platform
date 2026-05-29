package com.finance.platform.budget.web.dto;

import java.util.List;

public record TotalBudgetsSectionDto(String total, String filter, List<BudgetCategoryItemDto> categories) {
}
