package com.finance.platform.finance.web;

import com.finance.platform.finance.application.GetSubscriptionStatsQueryHandler;
import com.finance.platform.finance.application.ListSubscriptionsQueryHandler;
import com.finance.platform.finance.application.SubscriptionStats;
import com.finance.platform.finance.application.TransactionListResult;
import com.finance.platform.finance.application.TransactionWithAccount;
import com.finance.platform.finance.domain.RecurringFrequency;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionStatus;
import com.finance.platform.finance.domain.TransactionType;
import com.finance.platform.security.PlatformSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
@Import(PlatformSecurityConfiguration.class)
class SubscriptionControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ListSubscriptionsQueryHandler listSubscriptionsQueryHandler;
    @MockBean private GetSubscriptionStatsQueryHandler subscriptionStatsQueryHandler;

    @Test
    void listSubscriptions_withoutAuth_isForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/finance/subscriptions"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listSubscriptions_withDevHeader_returnsPage() throws Exception {
        var tx = new Transaction(
                UUID.randomUUID(), UUID.randomUUID(), "seed-user-alice", UUID.randomUUID(),
                new BigDecimal("10.99"), "INR",
                TransactionType.DEBIT, TransactionStatus.SETTLED,
                "Spotify Premium", "Entertainment", "Monthly subscription",
                true, RecurringFrequency.MONTHLY, LocalDate.of(2025, 8, 15),
                LocalDate.of(2025, 7, 15));

        var result = new TransactionListResult(
                List.of(new TransactionWithAccount(tx, "HDFC Regalia Credit")),
                1L, 0, 20);

        when(listSubscriptionsQueryHandler.handle(any())).thenReturn(result);

        mockMvc.perform(get("/api/v1/finance/subscriptions")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Spotify Premium"))
                .andExpect(jsonPath("$.content[0].frequency").value("MONTHLY"))
                .andExpect(jsonPath("$.content[0].nextDueDate").value("2025-08-15"))
                .andExpect(jsonPath("$.content[0].accountName").value("HDFC Regalia Credit"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getStats_withDevHeader_returnsStats() throws Exception {
        when(subscriptionStatsQueryHandler.handle(any()))
                .thenReturn(new SubscriptionStats(
                        3L,
                        new BigDecimal("42.98"),
                        new BigDecimal("515.76"),
                        LocalDate.of(2025, 8, 15)));

        mockMvc.perform(get("/api/v1/finance/subscriptions/stats")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeCount").value(3))
                .andExpect(jsonPath("$.monthlyCost").value("42.98"))
                .andExpect(jsonPath("$.yearlyCost").value("515.76"))
                .andExpect(jsonPath("$.nextRenewal").value("2025-08-15"));
    }
}
