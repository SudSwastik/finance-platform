package com.finance.platform.budget.application;

import com.finance.platform.budget.domain.BudgetCategoryRepository;
import com.finance.platform.budget.domain.TotalBudgetsSnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetTotalBudgetsQueryHandler {

    private final BudgetCategoryRepository repository;

    public GetTotalBudgetsQueryHandler(BudgetCategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public TotalBudgetsSnapshot handle(GetTotalBudgetsQuery query) {
        var categories = repository.findByUserId(query.userId());
        var total = repository.findTotalDisplayByUserId(query.userId());
        return new TotalBudgetsSnapshot(total, "Expenses", categories);
    }
}
