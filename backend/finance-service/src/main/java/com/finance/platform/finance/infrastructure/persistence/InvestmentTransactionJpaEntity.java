package com.finance.platform.finance.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "investment_transactions", schema = "finance")
public class InvestmentTransactionJpaEntity {

    @Id
    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "asset_id", nullable = false)
    private UUID assetId;

    @Column(nullable = false, precision = 36, scale = 18)
    private BigDecimal quantity;

    @Column(name = "price_per_unit", nullable = false, precision = 36, scale = 18)
    private BigDecimal pricePerUnit;

    protected InvestmentTransactionJpaEntity() {}

    public UUID getTransactionId()      { return transactionId; }
    public UUID getAssetId()            { return assetId; }
    public BigDecimal getQuantity()     { return quantity; }
    public BigDecimal getPricePerUnit() { return pricePerUnit; }

    public void setTransactionId(UUID transactionId)   { this.transactionId = transactionId; }
    public void setAssetId(UUID assetId)               { this.assetId = assetId; }
    public void setQuantity(BigDecimal quantity)       { this.quantity = quantity; }
    public void setPricePerUnit(BigDecimal price)      { this.pricePerUnit = price; }
}
