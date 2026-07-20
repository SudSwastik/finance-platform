package com.finance.platform.finance.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.finance.application.AccountDetail;
import com.finance.platform.finance.application.AccountWithBalance;
import com.finance.platform.finance.application.GetAccountDetailQuery;
import com.finance.platform.finance.application.GetAccountDetailQueryHandler;
import com.finance.platform.finance.application.ListAccountsQuery;
import com.finance.platform.finance.application.ListAccountsQueryHandler;
import com.finance.platform.finance.web.dto.AccountDetailDto;
import com.finance.platform.finance.web.dto.AccountDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance/accounts")
public class AccountController {

    private final ListAccountsQueryHandler listAccountsQueryHandler;
    private final GetAccountDetailQueryHandler accountDetailQueryHandler;

    public AccountController(
            ListAccountsQueryHandler listAccountsQueryHandler,
            GetAccountDetailQueryHandler accountDetailQueryHandler) {
        this.listAccountsQueryHandler  = listAccountsQueryHandler;
        this.accountDetailQueryHandler = accountDetailQueryHandler;
    }

    @GetMapping
    public List<AccountDto> listAccounts() {
        String userSub = TenantContext.requireUserSub();
        return listAccountsQueryHandler.handle(new ListAccountsQuery(userSub))
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public AccountDetailDto getAccount(@PathVariable UUID id) {
        String userSub = TenantContext.requireUserSub();
        AccountDetail detail = accountDetailQueryHandler.handle(new GetAccountDetailQuery(userSub, id));
        return new AccountDetailDto(
                detail.account().id(),
                detail.account().type().name(),
                detail.account().name(),
                detail.account().currency(),
                detail.balance().toPlainString(),
                detail.monthChange().toPlainString(),
                detail.moneyInMonth().toPlainString(),
                detail.moneyOutMonth().toPlainString(),
                detail.avgDailyMonth().toPlainString());
    }

    private AccountDto toDto(AccountWithBalance a) {
        return new AccountDto(
                a.account().id(),
                a.account().type().name(),
                a.account().name(),
                a.account().currency(),
                a.balance().toPlainString());
    }
}
