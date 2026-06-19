package com.finance.platform.identity.application;

import java.util.UUID;

public record UserProfile(UUID userId, UUID tenantId, String userSub, String email) {}
