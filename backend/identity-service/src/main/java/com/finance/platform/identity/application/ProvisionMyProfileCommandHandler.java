package com.finance.platform.identity.application;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.identity.domain.Tenant;
import com.finance.platform.identity.domain.TenantRepository;
import com.finance.platform.identity.domain.TenantType;
import com.finance.platform.identity.domain.User;
import com.finance.platform.identity.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProvisionMyProfileCommandHandler {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public ProvisionMyProfileCommandHandler(TenantRepository tenantRepository, UserRepository userRepository) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
    }

    /** Idempotent: a sub that's already provisioned returns its existing profile unchanged. */
    @Transactional
    public UserProfile handle(ProvisionMyProfileCommand command) {
        String userSub = TenantContext.requireUserSub();

        return userRepository.findByUserSub(userSub)
                .map(this::toProfile)
                .orElseGet(() -> provision(userSub, command));
    }

    private UserProfile provision(String userSub, ProvisionMyProfileCommand command) {
        TenantType type = "business".equals(command.accountType()) ? TenantType.ORG : TenantType.PERSONAL;
        String label = "business".equals(command.accountType()) ? "Business" : "Personal";

        Tenant tenant = new Tenant(UUID.randomUUID(), command.name() + " " + label, type);
        tenantRepository.save(tenant);

        User user = new User(UUID.randomUUID(), tenant.id(), userSub, command.email(), "USER");
        userRepository.save(user);

        return toProfile(user);
    }

    private UserProfile toProfile(User user) {
        return new UserProfile(user.id(), user.tenantId(), user.userSub(), user.email());
    }
}
