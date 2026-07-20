package com.finance.platform.finance.web;

import com.finance.platform.finance.application.ListTradesQueryHandler;
import com.finance.platform.finance.application.TradeWithDetails;
import com.finance.platform.finance.domain.Asset;
import com.finance.platform.finance.domain.AssetType;
import com.finance.platform.finance.domain.InvestmentTransaction;
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

@WebMvcTest(TradeController.class)
@Import(PlatformSecurityConfiguration.class)
class TradeControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ListTradesQueryHandler listTradesQueryHandler;

    @Test
    void listTrades_withoutAuth_isForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/finance/trades"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listTrades_withDevHeader_returnsTrades() throws Exception {
        var assetId = UUID.randomUUID();
        var tx = new Transaction(
                UUID.randomUUID(), UUID.randomUUID(), "seed-user-alice", UUID.randomUUID(),
                new BigDecimal("42000.00"), "INR",
                TransactionType.BUY, TransactionStatus.SETTLED,
                "Zerodha", "Investment", "BTC purchase",
                false, LocalDate.of(2026, 6, 10));

        var asset = new Asset(assetId, "BTC", "Bitcoin", AssetType.CRYPTO);
        var inv = new InvestmentTransaction(tx.id(), assetId, new BigDecimal("0.050000000000000000"), new BigDecimal("840000.000000000000000000"));

        when(listTradesQueryHandler.handle(any()))
                .thenReturn(List.of(new TradeWithDetails(tx, inv, asset, "Zerodha Demat")));

        mockMvc.perform(get("/api/v1/finance/trades")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].side").value("BUY"))
                .andExpect(jsonPath("$[0].assetSymbol").value("BTC"))
                .andExpect(jsonPath("$[0].quantity").value("0.05"))
                .andExpect(jsonPath("$[0].pricePerUnit").value("840000"))
                .andExpect(jsonPath("$[0].amount").value("42000.00"))
                .andExpect(jsonPath("$[0].accountName").value("Zerodha Demat"));
    }
}
