package com.finance.platform.finance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

interface InvestmentTransactionJpaRepository extends JpaRepository<InvestmentTransactionJpaEntity, UUID> {

    List<InvestmentTransactionJpaEntity> findAllByTransactionIdIn(Set<UUID> transactionIds);
}
