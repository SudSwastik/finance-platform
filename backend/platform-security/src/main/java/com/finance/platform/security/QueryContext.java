package com.finance.platform.security;

public final class QueryContext {

    public enum Scope { OWN, TENANT, PLATFORM }

    private static final ThreadLocal<QueryContext> HOLDER = new ThreadLocal<>();

    private final String userSub;
    private final String tenantId;
    private final Scope scope;

    private QueryContext(String userSub, String tenantId, Scope scope) {
        this.userSub = userSub;
        this.tenantId = tenantId;
        this.scope = scope;
    }

    public static QueryContext own(String userSub) {
        return new QueryContext(userSub, null, Scope.OWN);
    }

    public static QueryContext tenant(String userSub, String tenantId) {
        return new QueryContext(userSub, tenantId, Scope.TENANT);
    }

    public static QueryContext platform(String userSub) {
        return new QueryContext(userSub, null, Scope.PLATFORM);
    }

    public static void set(QueryContext ctx) {
        HOLDER.set(ctx);
    }

    public static QueryContext require() {
        QueryContext ctx = HOLDER.get();
        if (ctx == null) {
            throw new IllegalStateException("QueryContext not set — authentication filter did not run");
        }
        return ctx;
    }

    public static void clear() {
        HOLDER.remove();
    }

    public String userSub() {
        return userSub;
    }

    public Scope scope() {
        return scope;
    }

    /** Present when scope is TENANT or PLATFORM (set from JWT tenant_id claim). */
    public String tenantIdOrNull() {
        return tenantId;
    }

    public String requireTenantId() {
        if (tenantId == null) {
            throw new IllegalStateException("tenantId not in QueryContext (scope=" + scope + ")");
        }
        return tenantId;
    }
}
