package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Transaction;

import java.math.BigDecimal;

public final class TransactionSigns {

    private TransactionSigns() {}

    // TRANSFER is treated as a pure outflow: this ledger stores one row per
    // transfer with no offsetting entry on a destination account.
    public static BigDecimal signedAmount(Transaction t) {
        return switch (t.type()) {
            case CREDIT, SELL -> t.amount();
            case DEBIT, FEE, TRANSFER, BUY -> t.amount().negate();
        };
    }
}
