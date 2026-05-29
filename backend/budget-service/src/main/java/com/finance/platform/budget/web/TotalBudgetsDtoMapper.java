package com.finance.platform.budget.web;

import com.finance.platform.budget.web.dto.BudgetCategoryItemDto;
import com.finance.platform.budget.web.dto.TotalBudgetsSectionDto;
import com.finance.platform.budget.domain.BudgetCategory;
import com.finance.platform.budget.domain.TotalBudgetsSnapshot;
import org.springframework.stereotype.Component;

@Component
public class TotalBudgetsDtoMapper {

    public TotalBudgetsSectionDto toDto(TotalBudgetsSnapshot snapshot) {
        var items = snapshot.categories().stream().map(this::toItem).toList();
        return new TotalBudgetsSectionDto(
                snapshot.totalDisplay().toApiString(),
                snapshot.filter(),
                items);
    }

    private BudgetCategoryItemDto toItem(BudgetCategory category) {
        return new BudgetCategoryItemDto(
                category.id().toString(),
                category.name(),
                category.colorToken(),
                category.spent().toApiString(),
                category.budgetCap().toApiString(),
                category.percentUsed());
    }
}
