package com.finance.platform.finance.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class Transaction {

    private final UUID id;
    private final UUID tenantId;
    private final String userSub;
    private final UUID accountId;
    private final BigDecimal amount;
    private final String currency;
    private final TransactionType type;
    private final TransactionStatus status;
    private final String merchantName;
    private final String category;
    private final String description;
    private final boolean recurring;
    private final LocalDate transactionDate;

    public Transaction(
            UUID id, UUID tenantId, String userSub, UUID accountId,
            BigDecimal amount, String currency,
            TransactionType type, TransactionStatus status,
            String merchantName, String category, String description,
            boolean recurring, LocalDate transactionDate) {
        this.id              = Objects.requireNonNull(id);
        this.tenantId        = Objects.requireNonNull(tenantId);
        this.userSub         = Objects.requireNonNull(userSub);
        this.accountId       = Objects.requireNonNull(accountId);
        this.amount          = Objects.requireNonNull(amount);
        this.currency        = Objects.requireNonNull(currency);
        this.type            = Objects.requireNonNull(type);
        this.status          = Objects.requireNonNull(status);
        this.merchantName    = merchantName;
        this.category        = category;
        this.description     = description;
        this.recurring       = recurring;
        this.transactionDate = Objects.requireNonNull(transactionDate);
    }

    public UUID id()                  { return id; }
    public UUID tenantId()            { return tenantId; }
    public String userSub()           { return userSub; }
    public UUID accountId()           { return accountId; }
    public BigDecimal amount()        { return amount; }
    public String currency()          { return currency; }
    public TransactionType type()     { return type; }
    public TransactionStatus status() { return status; }
    public String merchantName()      { return merchantName; }
    public String category()          { return category; }
    public String description()       { return description; }
    public boolean isRecurring()      { return recurring; }
    public LocalDate transactionDate() { return transactionDate; }
}
