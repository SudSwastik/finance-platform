package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListRecurringTransactionsQueryHandler {

    private final TransactionRepository transactionRepository;

    public ListRecurringTransactionsQueryHandler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> handle(ListRecurringTransactionsQuery query) {
        return transactionRepository.findRecurringByUserSub(query.userSub());
    }
}
