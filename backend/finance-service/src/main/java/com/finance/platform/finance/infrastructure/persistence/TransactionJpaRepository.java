package com.finance.platform.finance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, UUID> {

    List<TransactionJpaEntity> findAllByUserSub(String userSub);

    List<TransactionJpaEntity> findAllByUserSubAndRecurringTrue(String userSub);

    List<TransactionJpaEntity> findAllByAccountIdAndUserSub(UUID accountId, String userSub);
}
