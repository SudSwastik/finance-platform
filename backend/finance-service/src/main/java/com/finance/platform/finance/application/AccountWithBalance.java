package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Account;

import java.math.BigDecimal;

public record AccountWithBalance(Account account, BigDecimal balance) {}
