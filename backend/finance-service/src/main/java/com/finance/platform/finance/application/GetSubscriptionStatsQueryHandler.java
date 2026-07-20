package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.RecurringFrequency;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
public class GetSubscriptionStatsQueryHandler {

    private static final BigDecimal WEEKS_PER_MONTH = new BigDecimal("4.33");
    private static final BigDecimal MONTHS_PER_YEAR  = BigDecimal.valueOf(12);

    private final TransactionRepository transactionRepository;

    public GetSubscriptionStatsQueryHandler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public SubscriptionStats handle(GetSubscriptionStatsQuery query) {
        List<Transaction> subscriptions = transactionRepository.findRecurringByUserSub(query.userSub());

        BigDecimal monthlyCost = subscriptions.stream()
                .map(this::monthlyEquivalent)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal yearlyCost = monthlyCost.multiply(MONTHS_PER_YEAR).setScale(2, RoundingMode.HALF_UP);

        var nextRenewal = subscriptions.stream()
                .map(Transaction::nextDueDate)
                .filter(java.util.Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

        return new SubscriptionStats(subscriptions.size(), monthlyCost, yearlyCost, nextRenewal);
    }

    private BigDecimal monthlyEquivalent(Transaction t) {
        RecurringFrequency frequency = t.recurringFrequency() != null
                ? t.recurringFrequency()
                : RecurringFrequency.MONTHLY;

        return switch (frequency) {
            case WEEKLY  -> t.amount().multiply(WEEKS_PER_MONTH);
            case MONTHLY -> t.amount();
            case YEARLY  -> t.amount().divide(MONTHS_PER_YEAR, 4, RoundingMode.HALF_UP);
        };
    }
}
