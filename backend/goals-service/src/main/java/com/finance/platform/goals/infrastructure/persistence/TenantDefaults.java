package com.finance.platform.goals.infrastructure.persistence;

import com.finance.platform.security.QueryContext;

import java.util.UUID;

/**
 * Phase 7 will populate real tenant_id from Cognito via QueryContext (TENANT/PLATFORM scope).
 * Until then dev-auth only sets OWN scope, so writes fall back to the same placeholder tenant
 * used by the other services' Phase 5 migrations.
 */
final class TenantDefaults {

    static final UUID PLACEHOLDER_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private TenantDefaults() {
    }

    static UUID currentOrPlaceholder() {
        String tenantId = QueryContext.require().tenantIdOrNull();
        return tenantId != null ? UUID.fromString(tenantId) : PLACEHOLDER_TENANT_ID;
    }
}
