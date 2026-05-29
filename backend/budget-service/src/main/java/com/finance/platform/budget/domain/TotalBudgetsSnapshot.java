package com.finance.platform.budget.domain;

import com.finance.platform.common.domain.Money;

import java.util.List;

public record TotalBudgetsSnapshot(Money totalDisplay, String filter, List<BudgetCategory> categories) {
}
