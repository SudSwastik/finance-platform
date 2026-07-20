package com.finance.platform.finance.infrastructure.persistence;

import com.finance.platform.finance.domain.AssetType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "assets", schema = "finance")
public class AssetJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 16)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false, length = 8)
    private AssetType assetType;

    protected AssetJpaEntity() {}

    public UUID getId()          { return id; }
    public String getSymbol()    { return symbol; }
    public String getName()      { return name; }
    public AssetType getAssetType() { return assetType; }

    public void setId(UUID id)                 { this.id = id; }
    public void setSymbol(String symbol)       { this.symbol = symbol; }
    public void setName(String name)           { this.name = name; }
    public void setAssetType(AssetType type)   { this.assetType = type; }
}
