package com.finance.platform.bff.web.dto;

import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;

import java.util.List;

public record OverviewResponseDto(
        TotalBudgetsSectionDto totalBudgets,
        List<HoldingItemDto> investments,
        List<RecurringItemDto> recurring,
        List<RecentTransactionItemDto> recentTransactions,
        MonthlySummaryDto monthlySummary) {
}
