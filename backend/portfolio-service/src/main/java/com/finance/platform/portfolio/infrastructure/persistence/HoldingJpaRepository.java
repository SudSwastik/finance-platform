package com.finance.platform.portfolio.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HoldingJpaRepository extends JpaRepository<HoldingJpaEntity, UUID> {
    List<HoldingJpaEntity> findByUserSubOrderBySymbolAsc(String userSub);
}
