package com.finance.platform.identity.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.identity.application.GetUserProfileQuery;
import com.finance.platform.identity.application.GetUserProfileQueryHandler;
import com.finance.platform.identity.application.UserProfile;
import com.finance.platform.identity.web.dto.UserProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {

    private final GetUserProfileQueryHandler queryHandler;

    public IdentityController(GetUserProfileQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMe() {
        String userSub = TenantContext.requireUserSub();
        UserProfile profile = queryHandler.handle(new GetUserProfileQuery(userSub));
        return ResponseEntity.ok(new UserProfileDto(
                profile.userId(), profile.tenantId(), profile.userSub(), profile.email()));
    }
}
