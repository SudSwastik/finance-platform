package com.finance.platform.budget.web;

import com.finance.platform.budget.web.dto.TotalBudgetsSectionDto;
import com.finance.platform.budget.application.GetTotalBudgetsQuery;
import com.finance.platform.budget.application.GetTotalBudgetsQueryHandler;
import com.finance.platform.security.SecurityContextUserIdResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final GetTotalBudgetsQueryHandler queryHandler;
    private final TotalBudgetsDtoMapper mapper;
    private final SecurityContextUserIdResolver userIdResolver;

    public BudgetController(
            GetTotalBudgetsQueryHandler queryHandler,
            TotalBudgetsDtoMapper mapper,
            SecurityContextUserIdResolver userIdResolver) {
        this.queryHandler = queryHandler;
        this.mapper = mapper;
        this.userIdResolver = userIdResolver;
    }

    @GetMapping("/total-budgets")
    public TotalBudgetsSectionDto getTotalBudgets() {
        var userId = userIdResolver.requireCurrentUserId();
        var snapshot = queryHandler.handle(new GetTotalBudgetsQuery(userId));
        return mapper.toDto(snapshot);
    }
}
