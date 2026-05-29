package com.finance.platform.bff.support;

import com.finance.platform.bff.web.dto.GoalItemDto;
import com.finance.platform.bff.web.dto.HoldingItemDto;
import com.finance.platform.bff.web.dto.RecurringItemDto;
import com.finance.platform.bff.web.dto.SpendingSectionDto;
import com.finance.platform.bff.web.dto.TransactionItemDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Temporary stubs until goals-service, ledger-service, etc. exist. Replace with HTTP clients per module.
 */
@Component
public class OverviewStubSections {

    public SpendingSectionDto spending() {
        return new SpendingSectionDto(
                "350.00",
                "This month vs. last month",
                List.of(
                        new SpendingSectionDto.SpendingPointDto(LocalDate.of(2025, 7, 11), "350.00", "280.00")));
    }

    public List<GoalItemDto> goals() {
        return List.of(
                new GoalItemDto("g1", "Vacation", "2300", "3000", new BigDecimal("76.7"), "category.essentials", "Nov 2025"));
    }

    public List<TransactionItemDto> transactions() {
        return List.of(
                new TransactionItemDto("t1", "Groceries", "essential", "-128.45", LocalDate.of(2025, 7, 11)));
    }

    public List<HoldingItemDto> investments() {
        return List.of(
                new HoldingItemDto("h1", "AAPL", "Apple", "1600.00", new BigDecimal("21.9"), "1950.00"));
    }

    public List<RecurringItemDto> recurring() {
        return List.of(
                new RecurringItemDto("r1", "Spotify Premium", "monthly", "10.99", LocalDate.of(2025, 7, 15)));
    }
}
