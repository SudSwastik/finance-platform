package com.finance.platform.finance.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.finance.application.GetMonthlySummaryQuery;
import com.finance.platform.finance.application.GetMonthlySummaryQueryHandler;
import com.finance.platform.finance.application.GetRecentTransactionsQuery;
import com.finance.platform.finance.application.GetRecentTransactionsQueryHandler;
import com.finance.platform.finance.application.ListRecurringTransactionsQuery;
import com.finance.platform.finance.application.ListRecurringTransactionsQueryHandler;
import com.finance.platform.finance.application.MonthlySummary;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.web.dto.MonthlySummaryDto;
import com.finance.platform.finance.web.dto.RecentTransactionDto;
import com.finance.platform.finance.web.dto.RecurringTransactionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/transactions")
public class TransactionController {

    private final ListRecurringTransactionsQueryHandler recurringQueryHandler;
    private final GetRecentTransactionsQueryHandler recentQueryHandler;
    private final GetMonthlySummaryQueryHandler monthlySummaryQueryHandler;

    public TransactionController(
            ListRecurringTransactionsQueryHandler recurringQueryHandler,
            GetRecentTransactionsQueryHandler recentQueryHandler,
            GetMonthlySummaryQueryHandler monthlySummaryQueryHandler) {
        this.recurringQueryHandler       = recurringQueryHandler;
        this.recentQueryHandler          = recentQueryHandler;
        this.monthlySummaryQueryHandler  = monthlySummaryQueryHandler;
    }

    @GetMapping("/recurring")
    public List<RecurringTransactionDto> listRecurring() {
        String userSub = TenantContext.requireUserSub();
        return recurringQueryHandler.handle(new ListRecurringTransactionsQuery(userSub))
                .stream().map(this::toRecurringDto).toList();
    }

    @GetMapping("/recent")
    public List<RecentTransactionDto> listRecent() {
        String userSub = TenantContext.requireUserSub();
        return recentQueryHandler.handle(new GetRecentTransactionsQuery(userSub))
                .stream().map(this::toRecentDto).toList();
    }

    @GetMapping("/monthly-summary")
    public MonthlySummaryDto getMonthlySummary() {
        String userSub = TenantContext.requireUserSub();
        MonthlySummary summary = monthlySummaryQueryHandler.handle(
                new GetMonthlySummaryQuery(userSub, YearMonth.now()));
        return new MonthlySummaryDto(
                summary.income().toPlainString(),
                summary.spending().toPlainString(),
                summary.netSaved().toPlainString());
    }

    private RecurringTransactionDto toRecurringDto(Transaction t) {
        return new RecurringTransactionDto(
                t.merchantName() != null ? t.merchantName() : t.description(),
                "Monthly",
                t.amount().toPlainString(),
                t.transactionDate().toString());
    }

    private RecentTransactionDto toRecentDto(Transaction t) {
        return new RecentTransactionDto(
                t.id().toString(),
                t.merchantName() != null ? t.merchantName() : t.description(),
                t.category(),
                t.type().name(),
                t.amount().toPlainString(),
                t.transactionDate().toString());
    }
}
