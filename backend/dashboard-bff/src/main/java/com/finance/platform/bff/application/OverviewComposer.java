package com.finance.platform.bff.application;

import com.finance.platform.bff.web.dto.OverviewResponseDto;
import com.finance.platform.bff.client.BudgetServiceClient;
import com.finance.platform.bff.support.OverviewStubSections;
import com.finance.platform.common.domain.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OverviewComposer {

    private final BudgetServiceClient budgetServiceClient;
    private final OverviewStubSections stubSections;

    public OverviewComposer(BudgetServiceClient budgetServiceClient, OverviewStubSections stubSections) {
        this.budgetServiceClient = budgetServiceClient;
        this.stubSections = stubSections;
    }

    public Mono<OverviewResponseDto> compose(UserId userId) {
        return budgetServiceClient
                .getTotalBudgets(userId, userId.value())
                .map(totalBudgets -> new OverviewResponseDto(
                        totalBudgets,
                        stubSections.spending(),
                        stubSections.goals(),
                        stubSections.transactions(),
                        stubSections.investments(),
                        stubSections.recurring()));
    }
}
