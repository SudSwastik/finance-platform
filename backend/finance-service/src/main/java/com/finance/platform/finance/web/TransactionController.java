package com.finance.platform.finance.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.finance.application.GetMonthlySummaryQuery;
import com.finance.platform.finance.application.GetMonthlySummaryQueryHandler;
import com.finance.platform.finance.application.GetRecentTransactionsQuery;
import com.finance.platform.finance.application.GetRecentTransactionsQueryHandler;
import com.finance.platform.finance.application.GetTransactionStatsQuery;
import com.finance.platform.finance.application.GetTransactionStatsQueryHandler;
import com.finance.platform.finance.application.ListRecurringTransactionsQuery;
import com.finance.platform.finance.application.ListRecurringTransactionsQueryHandler;
import com.finance.platform.finance.application.ListTransactionsQuery;
import com.finance.platform.finance.application.ListTransactionsQueryHandler;
import com.finance.platform.finance.application.MonthlySummary;
import com.finance.platform.finance.application.TransactionListResult;
import com.finance.platform.finance.application.TransactionStats;
import com.finance.platform.finance.application.TransactionWithAccount;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionFilter;
import com.finance.platform.finance.web.dto.MonthlySummaryDto;
import com.finance.platform.finance.web.dto.RecentTransactionDto;
import com.finance.platform.finance.web.dto.RecurringTransactionDto;
import com.finance.platform.finance.web.dto.TransactionDto;
import com.finance.platform.finance.web.dto.TransactionPageDto;
import com.finance.platform.finance.web.dto.TransactionStatsDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance/transactions")
public class TransactionController {

    private final ListRecurringTransactionsQueryHandler recurringQueryHandler;
    private final GetRecentTransactionsQueryHandler recentQueryHandler;
    private final GetMonthlySummaryQueryHandler monthlySummaryQueryHandler;
    private final ListTransactionsQueryHandler listTransactionsQueryHandler;
    private final GetTransactionStatsQueryHandler transactionStatsQueryHandler;

    public TransactionController(
            ListRecurringTransactionsQueryHandler recurringQueryHandler,
            GetRecentTransactionsQueryHandler recentQueryHandler,
            GetMonthlySummaryQueryHandler monthlySummaryQueryHandler,
            ListTransactionsQueryHandler listTransactionsQueryHandler,
            GetTransactionStatsQueryHandler transactionStatsQueryHandler) {
        this.recurringQueryHandler          = recurringQueryHandler;
        this.recentQueryHandler             = recentQueryHandler;
        this.monthlySummaryQueryHandler     = monthlySummaryQueryHandler;
        this.listTransactionsQueryHandler   = listTransactionsQueryHandler;
        this.transactionStatsQueryHandler   = transactionStatsQueryHandler;
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

    @GetMapping
    public TransactionPageDto listTransactions(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String typeGroup,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {

        String userSub = TenantContext.requireUserSub();
        TransactionFilter filter = new TransactionFilter(
                typeGroup,
                search,
                month != null ? YearMonth.parse(month) : null,
                accountId != null ? UUID.fromString(accountId) : null,
                category,
                status,
                false,
                null);

        TransactionListResult result = listTransactionsQueryHandler.handle(
                new ListTransactionsQuery(userSub, filter, page, size));

        List<TransactionDto> dtos = result.content().stream()
                .map(this::toTransactionDto)
                .toList();

        int totalPages = size > 0 ? (int) Math.ceil((double) result.totalElements() / size) : 0;

        return new TransactionPageDto(dtos, result.totalElements(), result.page(), result.size(), totalPages);
    }

    @GetMapping("/stats")
    public TransactionStatsDto getStats(@RequestParam(required = false) String month) {
        String userSub = TenantContext.requireUserSub();
        YearMonth yearMonth = month != null ? YearMonth.parse(month) : YearMonth.now();
        TransactionStats stats = transactionStatsQueryHandler.handle(
                new GetTransactionStatsQuery(userSub, yearMonth));
        return new TransactionStatsDto(
                stats.moneyIn().toPlainString(),
                stats.moneyInCount(),
                stats.moneyOut().toPlainString(),
                stats.moneyOutCount(),
                stats.netFlow().toPlainString(),
                stats.totalCount());
    }

    private TransactionDto toTransactionDto(TransactionWithAccount twa) {
        Transaction t = twa.transaction();
        return new TransactionDto(
                t.id().toString(),
                t.merchantName() != null ? t.merchantName() : t.description(),
                t.category(),
                t.type().name(),
                t.status().name(),
                t.amount().toPlainString(),
                t.transactionDate().toString(),
                twa.accountName());
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
