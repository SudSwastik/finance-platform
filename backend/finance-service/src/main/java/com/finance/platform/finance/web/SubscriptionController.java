package com.finance.platform.finance.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.finance.application.GetSubscriptionStatsQuery;
import com.finance.platform.finance.application.GetSubscriptionStatsQueryHandler;
import com.finance.platform.finance.application.ListSubscriptionsQuery;
import com.finance.platform.finance.application.ListSubscriptionsQueryHandler;
import com.finance.platform.finance.application.SubscriptionStats;
import com.finance.platform.finance.application.TransactionListResult;
import com.finance.platform.finance.application.TransactionWithAccount;
import com.finance.platform.finance.domain.RecurringFrequency;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionFilter;
import com.finance.platform.finance.web.dto.SubscriptionDto;
import com.finance.platform.finance.web.dto.SubscriptionPageDto;
import com.finance.platform.finance.web.dto.SubscriptionStatsDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/subscriptions")
public class SubscriptionController {

    private final ListSubscriptionsQueryHandler listSubscriptionsQueryHandler;
    private final GetSubscriptionStatsQueryHandler subscriptionStatsQueryHandler;

    public SubscriptionController(
            ListSubscriptionsQueryHandler listSubscriptionsQueryHandler,
            GetSubscriptionStatsQueryHandler subscriptionStatsQueryHandler) {
        this.listSubscriptionsQueryHandler = listSubscriptionsQueryHandler;
        this.subscriptionStatsQueryHandler = subscriptionStatsQueryHandler;
    }

    @GetMapping
    public SubscriptionPageDto listSubscriptions(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String frequency,
            @RequestParam(required = false) String status) {

        String userSub = TenantContext.requireUserSub();
        TransactionFilter filter = new TransactionFilter(
                null,
                search,
                null,
                null,
                category,
                status,
                true,
                frequency != null ? RecurringFrequency.valueOf(frequency.toUpperCase()) : null);

        TransactionListResult result = listSubscriptionsQueryHandler.handle(
                new ListSubscriptionsQuery(userSub, filter, page, size));

        List<SubscriptionDto> dtos = result.content().stream()
                .map(this::toSubscriptionDto)
                .toList();

        int totalPages = size > 0 ? (int) Math.ceil((double) result.totalElements() / size) : 0;

        return new SubscriptionPageDto(dtos, result.totalElements(), result.page(), result.size(), totalPages);
    }

    @GetMapping("/stats")
    public SubscriptionStatsDto getStats() {
        String userSub = TenantContext.requireUserSub();
        SubscriptionStats stats = subscriptionStatsQueryHandler.handle(new GetSubscriptionStatsQuery(userSub));
        return new SubscriptionStatsDto(
                stats.activeCount(),
                stats.monthlyCost().toPlainString(),
                stats.yearlyCost().toPlainString(),
                stats.nextRenewal() != null ? stats.nextRenewal().toString() : null);
    }

    private SubscriptionDto toSubscriptionDto(TransactionWithAccount twa) {
        Transaction t = twa.transaction();
        return new SubscriptionDto(
                t.id().toString(),
                t.merchantName() != null ? t.merchantName() : t.description(),
                t.category(),
                t.amount().toPlainString(),
                t.currency(),
                t.recurringFrequency() != null ? t.recurringFrequency().name() : null,
                t.nextDueDate() != null ? t.nextDueDate().toString() : null,
                t.status().name(),
                twa.accountName());
    }
}
