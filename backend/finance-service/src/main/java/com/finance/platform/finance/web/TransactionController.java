package com.finance.platform.finance.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.finance.application.ListRecurringTransactionsQuery;
import com.finance.platform.finance.application.ListRecurringTransactionsQueryHandler;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.web.dto.RecurringTransactionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/transactions")
public class TransactionController {

    private final ListRecurringTransactionsQueryHandler recurringQueryHandler;

    public TransactionController(ListRecurringTransactionsQueryHandler recurringQueryHandler) {
        this.recurringQueryHandler = recurringQueryHandler;
    }

    @GetMapping("/recurring")
    public List<RecurringTransactionDto> listRecurring() {
        String userSub = TenantContext.requireUserSub();
        return recurringQueryHandler.handle(new ListRecurringTransactionsQuery(userSub))
                .stream()
                .map(this::toDto)
                .toList();
    }

    private RecurringTransactionDto toDto(Transaction t) {
        return new RecurringTransactionDto(
                t.merchantName() != null ? t.merchantName() : t.description(),
                "Monthly",
                t.amount().toPlainString(),
                t.transactionDate().toString());
    }
}
