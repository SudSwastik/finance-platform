package com.finance.platform.identity.infrastructure.persistence;

import com.finance.platform.identity.domain.Tenant;
import com.finance.platform.identity.domain.TenantRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class TenantRepositoryAdapter implements TenantRepository {

    private final TenantJpaRepository jpaRepository;

    TenantRepositoryAdapter(TenantJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void save(Tenant tenant) {
        jpaRepository.save(toEntity(tenant));
    }

    private Tenant toDomain(TenantJpaEntity e) {
        return new Tenant(e.getId(), e.getName(), e.getType());
    }

    private TenantJpaEntity toEntity(Tenant t) {
        TenantJpaEntity e = new TenantJpaEntity();
        e.setId(t.id());
        e.setName(t.name());
        e.setType(t.type());
        return e;
    }
}
