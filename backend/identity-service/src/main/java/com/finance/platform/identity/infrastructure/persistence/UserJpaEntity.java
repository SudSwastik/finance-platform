package com.finance.platform.identity.infrastructure.persistence;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users", schema = "identity")
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_sub", nullable = false, length = 128)
    private String userSub;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 16)
    private String role;

    protected UserJpaEntity() {}

    public UUID getId()       { return id; }
    public UUID getTenantId() { return tenantId; }
    public String getUserSub() { return userSub; }
    public String getEmail()  { return email; }
    public String getRole()   { return role; }

    public void setId(UUID id)              { this.id = id; }
    public void setTenantId(UUID tenantId)  { this.tenantId = tenantId; }
    public void setUserSub(String userSub)  { this.userSub = userSub; }
    public void setEmail(String email)      { this.email = email; }
    public void setRole(String role)        { this.role = role; }
}
