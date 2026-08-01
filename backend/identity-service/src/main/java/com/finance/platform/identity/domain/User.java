package com.finance.platform.identity.domain;

import java.util.Objects;
import java.util.UUID;

public final class User {

    private final UUID id;
    private final UUID tenantId;
    private final String userSub;
    private final String email;
    private final String role;

    public User(UUID id, UUID tenantId, String userSub, String email, String role) {
        this.id       = Objects.requireNonNull(id);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.userSub  = Objects.requireNonNull(userSub);
        this.email    = Objects.requireNonNull(email);
        this.role     = Objects.requireNonNull(role);
    }

    public UUID id()       { return id; }
    public UUID tenantId() { return tenantId; }
    public String userSub() { return userSub; }
    public String email()  { return email; }
    public String role()   { return role; }
}
