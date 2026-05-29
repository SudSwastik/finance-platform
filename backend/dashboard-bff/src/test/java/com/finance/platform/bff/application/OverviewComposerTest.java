package com.finance.platform.bff.application;

import com.finance.platform.bff.client.BudgetServiceClient;
import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;
import com.finance.platform.bff.support.OverviewStubSections;
import com.finance.platform.common.domain.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverviewComposerTest {

    @Mock
    private BudgetServiceClient budgetServiceClient;

    @Spy
    private OverviewStubSections stubSections = new OverviewStubSections();

    @InjectMocks
    private OverviewComposer composer;

    @Test
    void compose_mergesBudgetServiceWithStubs() {
        UserId userId = UserId.of("seed-user-alice");
        var totalBudgets = new TotalBudgetsSectionDto("6400", "Expenses", List.of());

        when(budgetServiceClient.getTotalBudgets(userId, userId.value())).thenReturn(reactor.core.publisher.Mono.just(totalBudgets));

        StepVerifier.create(composer.compose(userId))
                .assertNext(overview -> {
                    assert overview.totalBudgets().total().equals("6400");
                    assert overview.goals().size() == 1;
                    assert overview.transactions().size() == 1;
                })
                .verifyComplete();
    }
}
