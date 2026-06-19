package com.finance.platform.finance.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.finance.application.ListAccountsQuery;
import com.finance.platform.finance.application.ListAccountsQueryHandler;
import com.finance.platform.finance.domain.Account;
import com.finance.platform.finance.web.dto.AccountDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/accounts")
public class AccountController {

    private final ListAccountsQueryHandler queryHandler;

    public AccountController(ListAccountsQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    @GetMapping
    public List<AccountDto> listAccounts() {
        String userSub = TenantContext.requireUserSub();
        return queryHandler.handle(new ListAccountsQuery(userSub))
                .stream()
                .map(this::toDto)
                .toList();
    }

    private AccountDto toDto(Account a) {
        return new AccountDto(a.id(), a.type().name(), a.name(), a.currency());
    }
}
