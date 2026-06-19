package com.finance.platform.portfolio.infrastructure.persistence;

import com.finance.platform.common.domain.Money;
import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.portfolio.domain.Holding;
import com.finance.platform.portfolio.domain.HoldingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HoldingRepositoryAdapter implements HoldingRepository {

    private final HoldingJpaRepository jpaRepository;

    public HoldingRepositoryAdapter(HoldingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Holding> findAll() {
        String sub = TenantContext.requireUserSub();
        return jpaRepository.findByUserSubOrderBySymbolAsc(sub).stream()
                .map(this::toDomain)
                .toList();
    }

    private Holding toDomain(HoldingJpaEntity entity) {
        return new Holding(
                entity.getId(),
                entity.getSymbol(),
                entity.getName(),
                Money.of(entity.getCostBasis()),
                entity.getChangePercent(),
                Money.of(entity.getCurrentValue()));
    }
}
