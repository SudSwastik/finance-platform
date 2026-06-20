package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Transaction;

public record TransactionWithAccount(Transaction transaction, String accountName) {}
