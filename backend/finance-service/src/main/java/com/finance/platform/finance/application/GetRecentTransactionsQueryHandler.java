package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRecentTransactionsQueryHandler {

    private final TransactionRepository transactionRepository;

    public GetRecentTransactionsQueryHandler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> handle(GetRecentTransactionsQuery query) {
        return transactionRepository.findRecentByUserSub(query.userSub());
    }
}
