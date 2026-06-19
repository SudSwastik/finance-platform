package com.finance.platform.identity.infrastructure.persistence;

import com.finance.platform.identity.domain.TenantType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "identity")
public class TenantJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private TenantType type;

    protected TenantJpaEntity() {}

    public UUID getId()      { return id; }
    public String getName()  { return name; }
    public TenantType getType() { return type; }

    public void setId(UUID id)           { this.id = id; }
    public void setName(String name)     { this.name = name; }
    public void setType(TenantType type) { this.type = type; }
}
