package com.finance.platform.finance.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

interface TransactionJpaRepository
        extends JpaRepository<TransactionJpaEntity, UUID>,
                JpaSpecificationExecutor<TransactionJpaEntity> {

    List<TransactionJpaEntity> findAllByUserSub(String userSub);

    List<TransactionJpaEntity> findAllByUserSubAndRecurringTrue(String userSub);

    List<TransactionJpaEntity> findTop10ByUserSubOrderByTransactionDateDesc(String userSub);

    List<TransactionJpaEntity> findAllByUserSubAndTransactionDateBetween(String userSub, LocalDate from, LocalDate to);

    List<TransactionJpaEntity> findAllByAccountIdAndUserSub(UUID accountId, String userSub);
}
