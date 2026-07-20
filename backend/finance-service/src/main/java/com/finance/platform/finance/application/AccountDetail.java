package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Account;

import java.math.BigDecimal;

public record AccountDetail(
        Account account,
        BigDecimal balance,
        BigDecimal monthChange,
        BigDecimal moneyInMonth,
        BigDecimal moneyOutMonth,
        BigDecimal avgDailyMonth
) {}
