package com.finance.platform.finance.infrastructure.persistence;

import com.finance.platform.finance.domain.AccountType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "finance")
public class AccountJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_sub", nullable = false, length = 128)
    private String userSub;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AccountType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 3)
    private String currency;

    protected AccountJpaEntity() {}

    public UUID getId()         { return id; }
    public UUID getTenantId()   { return tenantId; }
    public String getUserSub()  { return userSub; }
    public AccountType getType() { return type; }
    public String getName()     { return name; }
    public String getCurrency() { return currency; }

    public void setId(UUID id)              { this.id = id; }
    public void setTenantId(UUID tenantId)  { this.tenantId = tenantId; }
    public void setUserSub(String userSub)  { this.userSub = userSub; }
    public void setType(AccountType type)   { this.type = type; }
    public void setName(String name)        { this.name = name; }
    public void setCurrency(String currency) { this.currency = currency; }
}
