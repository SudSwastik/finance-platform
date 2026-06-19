package com.finance.platform.budget.web;

import com.finance.platform.budget.application.GetTotalBudgetsQuery;
import com.finance.platform.budget.application.GetTotalBudgetsQueryHandler;
import com.finance.platform.budget.domain.BudgetCategory;
import com.finance.platform.budget.domain.TotalBudgetsSnapshot;
import com.finance.platform.budget.web.dto.TotalBudgetsSectionDto;
import com.finance.platform.common.domain.Money;
import com.finance.platform.security.PlatformSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BudgetController.class)
@Import({PlatformSecurityConfiguration.class, TotalBudgetsDtoMapper.class})
class BudgetControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetTotalBudgetsQueryHandler queryHandler;

    @MockBean
    private TotalBudgetsDtoMapper mapper;

    @Test
    void getTotalBudgets_withoutAuth_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/budgets/total-budgets"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTotalBudgets_withDevHeader_returns200() throws Exception {
        var snapshot = new TotalBudgetsSnapshot(
                Money.of("6400"),
                "Expenses",
                List.of(new BudgetCategory(
                        UUID.fromString("a1000001-0000-4000-8000-000000000001"),
                        "Essentials",
                        "category.essentials",
                        Money.of("1750"),
                        Money.of("2800"))));

        when(queryHandler.handle(any(GetTotalBudgetsQuery.class))).thenReturn(snapshot);
        when(mapper.toDto(snapshot)).thenReturn(
                new TotalBudgetsSectionDto("6400", "Expenses", List.of()));

        mockMvc.perform(get("/api/v1/budgets/total-budgets")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value("6400"));
    }
}
