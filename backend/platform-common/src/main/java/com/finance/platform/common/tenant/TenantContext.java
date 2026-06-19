package com.finance.platform.common.tenant;

public final class TenantContext {

    private static final ThreadLocal<String> USER_SUB = new ThreadLocal<>();

    private TenantContext() {}

    public static String getUserSub() {
        return USER_SUB.get();
    }

    public static String requireUserSub() {
        String sub = USER_SUB.get();
        if (sub == null || sub.isBlank()) {
            throw new IllegalStateException("TenantContext has no user sub — authentication filter did not run");
        }
        return sub;
    }

    public static void set(String userSub) {
        USER_SUB.set(userSub);
    }

    public static void clear() {
        USER_SUB.remove();
    }
}
