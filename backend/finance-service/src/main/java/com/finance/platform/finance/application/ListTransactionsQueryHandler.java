package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Account;
import com.finance.platform.finance.domain.AccountRepository;
import com.finance.platform.finance.domain.TransactionPage;
import com.finance.platform.finance.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListTransactionsQueryHandler {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public ListTransactionsQueryHandler(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository     = accountRepository;
    }

    public TransactionListResult handle(ListTransactionsQuery query) {
        TransactionPage page = transactionRepository.findPage(
                query.userSub(), query.filter(), query.page(), query.size());

        Map<UUID, String> accountNames = accountRepository
                .findAllByUserSub(query.userSub())
                .stream()
                .collect(Collectors.toMap(Account::id, Account::name));

        var enriched = page.content().stream()
                .map(tx -> new TransactionWithAccount(
                        tx,
                        accountNames.getOrDefault(tx.accountId(), "")))
                .toList();

        return new TransactionListResult(enriched, page.totalElements(), page.page(), page.size());
    }
}
