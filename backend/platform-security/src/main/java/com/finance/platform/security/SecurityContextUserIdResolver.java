package com.finance.platform.security;

import com.finance.platform.common.domain.UserId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public class SecurityContextUserIdResolver {

    public UserId requireCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof PlatformUserPrincipal principal)) {
            throw new UnauthorizedUserException("Not authenticated");
        }
        return principal.userId();
    }
}
