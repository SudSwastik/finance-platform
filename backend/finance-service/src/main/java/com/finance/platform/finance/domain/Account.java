package com.finance.platform.finance.domain;

import java.util.Objects;
import java.util.UUID;

public final class Account {

    private final UUID id;
    private final UUID tenantId;
    private final String userSub;
    private final AccountType type;
    private final String name;
    private final String currency;

    public Account(UUID id, UUID tenantId, String userSub, AccountType type, String name, String currency) {
        this.id       = Objects.requireNonNull(id);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.userSub  = Objects.requireNonNull(userSub);
        this.type     = Objects.requireNonNull(type);
        this.name     = Objects.requireNonNull(name);
        this.currency = Objects.requireNonNull(currency);
    }

    public UUID id()         { return id; }
    public UUID tenantId()   { return tenantId; }
    public String userSub()  { return userSub; }
    public AccountType type() { return type; }
    public String name()     { return name; }
    public String currency() { return currency; }
}
