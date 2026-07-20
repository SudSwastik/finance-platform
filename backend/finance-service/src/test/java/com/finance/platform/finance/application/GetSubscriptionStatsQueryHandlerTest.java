package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.RecurringFrequency;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import com.finance.platform.finance.domain.TransactionStatus;
import com.finance.platform.finance.domain.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetSubscriptionStatsQueryHandlerTest {

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private final GetSubscriptionStatsQueryHandler handler = new GetSubscriptionStatsQueryHandler(transactionRepository);

    @Test
    void handle_normalizesWeeklyAndYearlyToMonthlyEquivalent() {
        Transaction weekly = subscription(
                new BigDecimal("5.00"), RecurringFrequency.WEEKLY, LocalDate.of(2026, 7, 1));
        Transaction monthly = subscription(
                new BigDecimal("20.00"), RecurringFrequency.MONTHLY, LocalDate.of(2026, 7, 15));
        Transaction yearly = subscription(
                new BigDecimal("120.00"), RecurringFrequency.YEARLY, LocalDate.of(2027, 1, 1));

        when(transactionRepository.findRecurringByUserSub(any()))
                .thenReturn(List.of(weekly, monthly, yearly));

        SubscriptionStats stats = handler.handle(new GetSubscriptionStatsQuery("seed-user-alice"));

        // weekly: 5.00 * 4.33 = 21.65, monthly: 20.00, yearly: 120.00 / 12 = 10.00
        assertThat(stats.activeCount()).isEqualTo(3);
        assertThat(stats.monthlyCost()).isEqualByComparingTo("51.65");
        assertThat(stats.yearlyCost()).isEqualByComparingTo("619.80");
        assertThat(stats.nextRenewal()).isEqualTo(LocalDate.of(2026, 7, 1));
    }

    @Test
    void handle_withNoSubscriptions_returnsZeroedStats() {
        when(transactionRepository.findRecurringByUserSub(any())).thenReturn(List.of());

        SubscriptionStats stats = handler.handle(new GetSubscriptionStatsQuery("seed-user-alice"));

        assertThat(stats.activeCount()).isZero();
        assertThat(stats.monthlyCost()).isEqualByComparingTo("0.00");
        assertThat(stats.yearlyCost()).isEqualByComparingTo("0.00");
        assertThat(stats.nextRenewal()).isNull();
    }

    private Transaction subscription(BigDecimal amount, RecurringFrequency frequency, LocalDate nextDueDate) {
        return new Transaction(
                UUID.randomUUID(), UUID.randomUUID(), "seed-user-alice", UUID.randomUUID(),
                amount, "INR",
                TransactionType.DEBIT, TransactionStatus.SETTLED,
                "Subscription", "Software", "Recurring charge",
                true, frequency, nextDueDate,
                LocalDate.of(2026, 6, 1));
    }
}
