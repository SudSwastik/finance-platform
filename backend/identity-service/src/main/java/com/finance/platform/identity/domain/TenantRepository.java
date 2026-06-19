package com.finance.platform.identity.domain;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {

    Optional<Tenant> findById(UUID id);

    void save(Tenant tenant);
}
