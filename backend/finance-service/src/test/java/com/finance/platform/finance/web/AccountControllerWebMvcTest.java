package com.finance.platform.finance.web;

import com.finance.platform.finance.application.AccountDetail;
import com.finance.platform.finance.application.AccountWithBalance;
import com.finance.platform.finance.application.GetAccountDetailQueryHandler;
import com.finance.platform.finance.application.ListAccountsQueryHandler;
import com.finance.platform.finance.domain.Account;
import com.finance.platform.finance.domain.AccountType;
import com.finance.platform.security.PlatformSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(PlatformSecurityConfiguration.class)
class AccountControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ListAccountsQueryHandler listAccountsQueryHandler;
    @MockBean private GetAccountDetailQueryHandler accountDetailQueryHandler;

    @Test
    void listAccounts_withoutAuth_isForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/finance/accounts"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listAccounts_withDevHeader_returnsAccountsWithBalance() throws Exception {
        var account = new Account(
                UUID.randomUUID(), UUID.randomUUID(), "seed-user-alice",
                AccountType.BANK, "HDFC Savings", "INR");

        when(listAccountsQueryHandler.handle(any()))
                .thenReturn(List.of(new AccountWithBalance(account, new BigDecimal("12480.50"))));

        mockMvc.perform(get("/api/v1/finance/accounts")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("HDFC Savings"))
                .andExpect(jsonPath("$[0].type").value("BANK"))
                .andExpect(jsonPath("$[0].balance").value("12480.50"));
    }

    @Test
    void getAccount_withDevHeader_returnsDetail() throws Exception {
        var accountId = UUID.randomUUID();
        var account = new Account(
                accountId, UUID.randomUUID(), "seed-user-alice",
                AccountType.BANK, "HDFC Savings", "INR");

        when(accountDetailQueryHandler.handle(any()))
                .thenReturn(new AccountDetail(
                        account,
                        new BigDecimal("12480.50"),
                        new BigDecimal("1240.00"),
                        new BigDecimal("8420.00"),
                        new BigDecimal("7180.00"),
                        new BigDecimal("239.30")));

        mockMvc.perform(get("/api/v1/finance/accounts/" + accountId)
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("12480.50"))
                .andExpect(jsonPath("$.monthChange").value("1240.00"))
                .andExpect(jsonPath("$.moneyInMonth").value("8420.00"))
                .andExpect(jsonPath("$.moneyOutMonth").value("7180.00"))
                .andExpect(jsonPath("$.avgDailyMonth").value("239.30"));
    }

    @Test
    void getAccount_notFound_returns404() throws Exception {
        when(accountDetailQueryHandler.handle(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        mockMvc.perform(get("/api/v1/finance/accounts/" + UUID.randomUUID())
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isNotFound());
    }
}
