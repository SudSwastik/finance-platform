package com.finance.platform.portfolio.application;

import com.finance.platform.common.domain.Money;
import com.finance.platform.portfolio.domain.Holding;
import com.finance.platform.portfolio.domain.HoldingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListHoldingsQueryHandlerTest {

    @Mock
    private HoldingRepository repository;

    @InjectMocks
    private ListHoldingsQueryHandler handler;

    @Test
    void handle_returnsHoldingsForCurrentTenant() {
        var holding = new Holding(UUID.randomUUID(), "AAPL", "Apple Inc.",
                Money.of("1600"), new BigDecimal("21.9"), Money.of("1950"));
        when(repository.findAll()).thenReturn(List.of(holding));

        var result = handler.handle(new ListHoldingsQuery());

        assertEquals(1, result.size());
        assertEquals("AAPL", result.getFirst().symbol());
    }
}
