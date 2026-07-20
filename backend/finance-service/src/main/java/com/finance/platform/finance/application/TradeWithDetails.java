package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Asset;
import com.finance.platform.finance.domain.InvestmentTransaction;
import com.finance.platform.finance.domain.Transaction;

public record TradeWithDetails(
        Transaction transaction,
        InvestmentTransaction investmentTransaction,
        Asset asset,
        String accountName
) {}
