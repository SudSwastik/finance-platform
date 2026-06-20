package com.finance.platform.bff.application;

import com.finance.platform.bff.client.BudgetServiceClient;
import com.finance.platform.bff.client.FinanceServiceClient;
import com.finance.platform.bff.client.PortfolioServiceClient;
import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;
import com.finance.platform.bff.web.dto.MonthlySummaryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverviewComposerTest {

    @Mock
    private BudgetServiceClient budgetServiceClient;
    @Mock
    private PortfolioServiceClient portfolioServiceClient;
    @Mock
    private FinanceServiceClient financeServiceClient;

    @InjectMocks
    private OverviewComposer composer;

    @Test
    void compose_mergesAllServicesIntoOverview() {
        String userSub = "seed-user-alice";
        var totalBudgets = new TotalBudgetsSectionDto("6400", "Expenses", List.of());
        var monthlySummary = new MonthlySummaryDto("85000.00", "45000.00", "40000.00");

        when(budgetServiceClient.getTotalBudgets(userSub)).thenReturn(Mono.just(totalBudgets));
        when(portfolioServiceClient.getHoldings(userSub)).thenReturn(Mono.just(List.of()));
        when(financeServiceClient.getRecurringTransactions(userSub)).thenReturn(Mono.just(List.of()));
        when(financeServiceClient.getRecentTransactions(userSub)).thenReturn(Mono.just(List.of()));
        when(financeServiceClient.getMonthlySummary(userSub)).thenReturn(Mono.just(monthlySummary));

        StepVerifier.create(composer.compose(userSub))
                .assertNext(overview -> {
                    assert overview.totalBudgets().total().equals("6400");
                    assert overview.investments() != null;
                    assert overview.recurring() != null;
                    assert overview.recentTransactions() != null;
                    assert overview.monthlySummary().income().equals("85000.00");
                })
                .verifyComplete();
    }
}
