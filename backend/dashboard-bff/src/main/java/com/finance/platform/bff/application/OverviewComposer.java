package com.finance.platform.bff.application;

import com.finance.platform.bff.client.BudgetServiceClient;
import com.finance.platform.bff.client.FinanceServiceClient;
import com.finance.platform.bff.client.PortfolioServiceClient;
import com.finance.platform.bff.web.dto.OverviewResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OverviewComposer {

    private final BudgetServiceClient budgetServiceClient;
    private final PortfolioServiceClient portfolioServiceClient;
    private final FinanceServiceClient financeServiceClient;

    public OverviewComposer(
            BudgetServiceClient budgetServiceClient,
            PortfolioServiceClient portfolioServiceClient,
            FinanceServiceClient financeServiceClient) {
        this.budgetServiceClient = budgetServiceClient;
        this.portfolioServiceClient = portfolioServiceClient;
        this.financeServiceClient = financeServiceClient;
    }

    public Mono<OverviewResponseDto> compose(String userSub) {
        return Mono.zip(
                budgetServiceClient.getTotalBudgets(userSub),
                portfolioServiceClient.getHoldings(userSub),
                financeServiceClient.getRecurringTransactions(userSub),
                financeServiceClient.getRecentTransactions(userSub),
                financeServiceClient.getMonthlySummary(userSub)
        ).map(t -> new OverviewResponseDto(t.getT1(), t.getT2(), t.getT3(), t.getT4(), t.getT5()));
    }
}
