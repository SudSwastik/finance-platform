package com.finance.platform.identity.web.dto;

import java.util.UUID;

public record UserProfileDto(UUID userId, UUID tenantId, String userSub, String email) {}
