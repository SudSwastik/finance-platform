package com.finance.platform.finance.web;

import com.finance.platform.finance.application.GetMonthlySummaryQueryHandler;
import com.finance.platform.finance.application.GetRecentTransactionsQueryHandler;
import com.finance.platform.finance.application.GetTransactionStatsQueryHandler;
import com.finance.platform.finance.application.ListRecurringTransactionsQueryHandler;
import com.finance.platform.finance.application.ListTransactionsQueryHandler;
import com.finance.platform.finance.application.MonthlySummary;
import com.finance.platform.finance.application.TransactionListResult;
import com.finance.platform.finance.application.TransactionStats;
import com.finance.platform.finance.application.TransactionWithAccount;
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

@WebMvcTest(TransactionController.class)
@Import(PlatformSecurityConfiguration.class)
class TransactionControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ListRecurringTransactionsQueryHandler queryHandler;
    @MockBean private GetRecentTransactionsQueryHandler recentQueryHandler;
    @MockBean private GetMonthlySummaryQueryHandler monthlySummaryQueryHandler;
    @MockBean private ListTransactionsQueryHandler listTransactionsQueryHandler;
    @MockBean private GetTransactionStatsQueryHandler transactionStatsQueryHandler;

    @Test
    void recurring_withoutAuth_isForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/finance/transactions/recurring"))
                .andExpect(status().isForbidden());
    }

    @Test
    void recurring_withDevHeader_returnsList() throws Exception {
        var tx = new Transaction(
                UUID.randomUUID(), UUID.randomUUID(), "seed-user-alice", UUID.randomUUID(),
                new BigDecimal("10.99"), "INR",
                TransactionType.DEBIT, TransactionStatus.SETTLED,
                "Spotify Premium", "Entertainment", "Monthly subscription",
                true, LocalDate.of(2025, 7, 15));

        when(queryHandler.handle(any())).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/v1/finance/transactions/recurring")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Spotify Premium"))
                .andExpect(jsonPath("$[0].amount").value("10.99"));
    }

    @Test
    void recent_withDevHeader_returnsList() throws Exception {
        var tx = new Transaction(
                UUID.randomUUID(), UUID.randomUUID(), "seed-user-alice", UUID.randomUUID(),
                new BigDecimal("84.20"), "INR",
                TransactionType.DEBIT, TransactionStatus.SETTLED,
                "Zepto", "Groceries", null,
                false, LocalDate.of(2026, 6, 10));

        when(recentQueryHandler.handle(any())).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/v1/finance/transactions/recent")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].merchantName").value("Zepto"))
                .andExpect(jsonPath("$[0].amount").value("84.20"));
    }

    @Test
    void monthlySummary_withDevHeader_returnsSummary() throws Exception {
        when(monthlySummaryQueryHandler.handle(any()))
                .thenReturn(new MonthlySummary(
                        new BigDecimal("85000.00"),
                        new BigDecimal("45000.00"),
                        new BigDecimal("40000.00")));

        mockMvc.perform(get("/api/v1/finance/transactions/monthly-summary")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.income").value("85000.00"))
                .andExpect(jsonPath("$.spending").value("45000.00"))
                .andExpect(jsonPath("$.netSaved").value("40000.00"));
    }

    @Test
    void listTransactions_withDevHeader_returnsPage() throws Exception {
        var tx = new Transaction(
                UUID.randomUUID(), UUID.randomUUID(), "seed-user-alice", UUID.randomUUID(),
                new BigDecimal("2840.00"), "INR",
                TransactionType.DEBIT, TransactionStatus.SETTLED,
                "Zepto", "Groceries", null,
                false, LocalDate.of(2026, 6, 17));

        var result = new TransactionListResult(
                List.of(new TransactionWithAccount(tx, "ICICI Credit Card")),
                1L, 0, 20);

        when(listTransactionsQueryHandler.handle(any())).thenReturn(result);

        mockMvc.perform(get("/api/v1/finance/transactions")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].merchantName").value("Zepto"))
                .andExpect(jsonPath("$.content[0].accountName").value("ICICI Credit Card"))
                .andExpect(jsonPath("$.content[0].status").value("SETTLED"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getStats_withDevHeader_returnsStats() throws Exception {
        when(transactionStatsQueryHandler.handle(any()))
                .thenReturn(new TransactionStats(
                        new BigDecimal("85000.00"), 1L,
                        new BigDecimal("75000.00"), 7L,
                        new BigDecimal("10000.00"), 8L));

        mockMvc.perform(get("/api/v1/finance/transactions/stats")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.moneyIn").value("85000.00"))
                .andExpect(jsonPath("$.moneyOut").value("75000.00"))
                .andExpect(jsonPath("$.netFlow").value("10000.00"))
                .andExpect(jsonPath("$.totalCount").value(8));
    }
}
