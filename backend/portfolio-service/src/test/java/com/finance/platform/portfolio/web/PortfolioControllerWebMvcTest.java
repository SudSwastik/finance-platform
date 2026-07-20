package com.finance.platform.portfolio.web;

import com.finance.platform.common.domain.Money;
import com.finance.platform.portfolio.application.ListHoldingsQuery;
import com.finance.platform.portfolio.application.ListHoldingsQueryHandler;
import com.finance.platform.portfolio.domain.AssetType;
import com.finance.platform.portfolio.domain.Holding;
import com.finance.platform.portfolio.web.dto.HoldingItemDto;
import com.finance.platform.security.PlatformSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PortfolioController.class)
@Import({PlatformSecurityConfiguration.class, HoldingDtoMapper.class})
class PortfolioControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListHoldingsQueryHandler queryHandler;

    @MockBean
    private HoldingDtoMapper mapper;

    @Test
    void listHoldings_withoutAuth_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/portfolio/holdings"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listHoldings_withDevHeader_returns200() throws Exception {
        var holding = new Holding(UUID.randomUUID(), "AAPL", "Apple Inc.", AssetType.STOCK,
                Money.of("1600"), new BigDecimal("21.9"), Money.of("1950"));
        var dto = new HoldingItemDto(holding.id(), "AAPL", "Apple Inc.", "STOCK", "1600", new BigDecimal("21.9"), "1950");

        when(queryHandler.handle(any(ListHoldingsQuery.class))).thenReturn(List.of(holding));
        when(mapper.toDto(holding)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/portfolio/holdings")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("AAPL"));
    }
}
