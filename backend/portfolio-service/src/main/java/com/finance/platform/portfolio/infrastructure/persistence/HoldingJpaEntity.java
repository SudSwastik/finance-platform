package com.finance.platform.portfolio.infrastructure.persistence;

import com.finance.platform.portfolio.domain.AssetType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(schema = "portfolio", name = "holdings")
public class HoldingJpaEntity {

    @Id
    private UUID id;

    @Column(name = "user_sub", nullable = false)
    private String userSub;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false, length = 8)
    private AssetType assetType;

    @Column(name = "cost_basis", nullable = false, precision = 19, scale = 2)
    private BigDecimal costBasis;

    @Column(name = "change_percent", nullable = false, precision = 6, scale = 2)
    private BigDecimal changePercent;

    @Column(name = "current_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentValue;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getUserSub() { return userSub; }
    public void setUserSub(String userSub) { this.userSub = userSub; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AssetType getAssetType() { return assetType; }
    public void setAssetType(AssetType assetType) { this.assetType = assetType; }
    public BigDecimal getCostBasis() { return costBasis; }
    public void setCostBasis(BigDecimal costBasis) { this.costBasis = costBasis; }
    public BigDecimal getChangePercent() { return changePercent; }
    public void setChangePercent(BigDecimal changePercent) { this.changePercent = changePercent; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }
}
