package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Account;
import com.finance.platform.finance.domain.AccountRepository;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListAccountsQueryHandler {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public ListAccountsQueryHandler(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository     = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<AccountWithBalance> handle(ListAccountsQuery query) {
        List<Account> accounts = accountRepository.findAllByUserSub(query.userSub());

        Map<UUID, BigDecimal> balancesByAccountId = transactionRepository
                .findAllByUserSub(query.userSub())
                .stream()
                .collect(Collectors.groupingBy(
                        Transaction::accountId,
                        Collectors.reducing(BigDecimal.ZERO, TransactionSigns::signedAmount, BigDecimal::add)));

        return accounts.stream()
                .map(a -> new AccountWithBalance(a, balancesByAccountId.getOrDefault(a.id(), BigDecimal.ZERO)))
                .toList();
    }
}
