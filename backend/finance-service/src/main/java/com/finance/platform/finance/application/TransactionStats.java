package com.finance.platform.finance.application;

import java.math.BigDecimal;

public record TransactionStats(
        BigDecimal moneyIn,
        long moneyInCount,
        BigDecimal moneyOut,
        long moneyOutCount,
        BigDecimal netFlow,
        long totalCount
) {}
