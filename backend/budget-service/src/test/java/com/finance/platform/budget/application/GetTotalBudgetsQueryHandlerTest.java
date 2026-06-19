package com.finance.platform.budget.application;

import com.finance.platform.budget.domain.BudgetCategory;
import com.finance.platform.budget.domain.BudgetCategoryRepository;
import com.finance.platform.common.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTotalBudgetsQueryHandlerTest {

    @Mock
    private BudgetCategoryRepository repository;

    @InjectMocks
    private GetTotalBudgetsQueryHandler handler;

    @Test
    void handle_returnsSnapshotForCurrentTenant() {
        var category = new BudgetCategory(
                UUID.fromString("a1000001-0000-4000-8000-000000000001"),
                "Essentials",
                "category.essentials",
                Money.of("1750.00"),
                Money.of("2800.00"));

        when(repository.findAll()).thenReturn(List.of(category));
        when(repository.findTotalDisplay()).thenReturn(Money.of("6400.00"));

        var result = handler.handle(new GetTotalBudgetsQuery());

        assertEquals("6400", result.totalDisplay().toApiString());
        assertEquals("Expenses", result.filter());
        assertEquals(1, result.categories().size());
        assertEquals("Essentials", result.categories().getFirst().name());
    }
}
