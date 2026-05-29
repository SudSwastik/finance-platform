package com.finance.platform.bff.web.dto;

import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;

import java.util.List;

public record OverviewResponseDto(
        TotalBudgetsSectionDto totalBudgets,
        SpendingSectionDto spending,
        List<GoalItemDto> goals,
        List<TransactionItemDto> transactions,
        List<HoldingItemDto> investments,
        List<RecurringItemDto> recurring) {
}
