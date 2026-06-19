package com.finance.platform.finance.infrastructure.persistence;

import com.finance.platform.finance.domain.TransactionStatus;
import com.finance.platform.finance.domain.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions", schema = "finance")
public class TransactionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_sub", nullable = false, length = 128)
    private String userSub;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private TransactionStatus status;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(length = 64)
    private String category;

    @Column
    private String description;

    @Column(name = "is_recurring", nullable = false)
    private boolean recurring;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "entry_source", nullable = false, length = 32)
    private String entrySource = "MANUAL";

    @Column(name = "created_by", nullable = false, length = 128)
    private String createdBy;

    protected TransactionJpaEntity() {}

    public UUID getId()                   { return id; }
    public UUID getTenantId()             { return tenantId; }
    public String getUserSub()            { return userSub; }
    public UUID getAccountId()            { return accountId; }
    public BigDecimal getAmount()         { return amount; }
    public String getCurrency()           { return currency; }
    public TransactionType getType()      { return type; }
    public TransactionStatus getStatus()  { return status; }
    public String getMerchantName()       { return merchantName; }
    public String getCategory()           { return category; }
    public String getDescription()        { return description; }
    public boolean isRecurring()          { return recurring; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public String getEntrySource()        { return entrySource; }
    public String getCreatedBy()          { return createdBy; }

    public void setId(UUID id)                            { this.id = id; }
    public void setTenantId(UUID tenantId)                { this.tenantId = tenantId; }
    public void setUserSub(String userSub)                { this.userSub = userSub; }
    public void setAccountId(UUID accountId)              { this.accountId = accountId; }
    public void setAmount(BigDecimal amount)              { this.amount = amount; }
    public void setCurrency(String currency)              { this.currency = currency; }
    public void setType(TransactionType type)             { this.type = type; }
    public void setStatus(TransactionStatus status)       { this.status = status; }
    public void setMerchantName(String merchantName)      { this.merchantName = merchantName; }
    public void setCategory(String category)              { this.category = category; }
    public void setDescription(String description)        { this.description = description; }
    public void setRecurring(boolean recurring)           { this.recurring = recurring; }
    public void setTransactionDate(LocalDate date)        { this.transactionDate = date; }
    public void setEntrySource(String entrySource)        { this.entrySource = entrySource; }
    public void setCreatedBy(String createdBy)            { this.createdBy = createdBy; }
}
