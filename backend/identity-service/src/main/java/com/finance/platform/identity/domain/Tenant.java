package com.finance.platform.identity.domain;

import java.util.Objects;
import java.util.UUID;

public final class Tenant {

    private final UUID id;
    private final String name;
    private final TenantType type;

    public Tenant(UUID id, String name, TenantType type) {
        this.id   = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    public UUID id()       { return id; }
    public String name()   { return name; }
    public TenantType type() { return type; }
}
