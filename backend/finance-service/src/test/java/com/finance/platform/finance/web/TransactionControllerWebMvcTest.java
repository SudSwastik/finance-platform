package com.finance.platform.finance.web;

import com.finance.platform.finance.application.ListRecurringTransactionsQueryHandler;
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

    @MockBean
    private ListRecurringTransactionsQueryHandler queryHandler;

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
}
