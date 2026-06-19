package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Account;
import com.finance.platform.finance.domain.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAccountsQueryHandler {

    private final AccountRepository accountRepository;

    public ListAccountsQueryHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> handle(ListAccountsQuery query) {
        return accountRepository.findAllByUserSub(query.userSub());
    }
}
