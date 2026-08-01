package com.finance.platform.identity.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.identity.application.GetUserProfileQuery;
import com.finance.platform.identity.application.GetUserProfileQueryHandler;
import com.finance.platform.identity.application.ProvisionMyProfileCommand;
import com.finance.platform.identity.application.ProvisionMyProfileCommandHandler;
import com.finance.platform.identity.application.UserProfile;
import com.finance.platform.identity.web.dto.ProvisionProfileRequestDto;
import com.finance.platform.identity.web.dto.UserProfileDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {

    private final GetUserProfileQueryHandler queryHandler;
    private final ProvisionMyProfileCommandHandler provisionCommandHandler;

    public IdentityController(
            GetUserProfileQueryHandler queryHandler,
            ProvisionMyProfileCommandHandler provisionCommandHandler) {
        this.queryHandler = queryHandler;
        this.provisionCommandHandler = provisionCommandHandler;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMe() {
        String userSub = TenantContext.requireUserSub();
        UserProfile profile = queryHandler.handle(new GetUserProfileQuery(userSub));
        return ResponseEntity.ok(toDto(profile));
    }

    @PostMapping("/me")
    public ResponseEntity<UserProfileDto> provisionMe(@Valid @RequestBody ProvisionProfileRequestDto request) {
        var command = new ProvisionMyProfileCommand(request.name(), request.email(), request.accountType());
        UserProfile profile = provisionCommandHandler.handle(command);
        return ResponseEntity.ok(toDto(profile));
    }

    private UserProfileDto toDto(UserProfile profile) {
        return new UserProfileDto(profile.userId(), profile.tenantId(), profile.userSub(), profile.email());
    }
}
