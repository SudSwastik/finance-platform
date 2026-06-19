package com.finance.platform.budget.web;

import com.finance.platform.budget.application.GetTotalBudgetsQuery;
import com.finance.platform.budget.application.GetTotalBudgetsQueryHandler;
import com.finance.platform.budget.web.dto.TotalBudgetsSectionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final GetTotalBudgetsQueryHandler queryHandler;
    private final TotalBudgetsDtoMapper mapper;

    public BudgetController(GetTotalBudgetsQueryHandler queryHandler, TotalBudgetsDtoMapper mapper) {
        this.queryHandler = queryHandler;
        this.mapper = mapper;
    }

    @GetMapping("/total-budgets")
    public TotalBudgetsSectionDto getTotalBudgets() {
        return mapper.toDto(queryHandler.handle(new GetTotalBudgetsQuery()));
    }
}
