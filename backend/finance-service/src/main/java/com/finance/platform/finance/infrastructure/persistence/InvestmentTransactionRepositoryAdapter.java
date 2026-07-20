package com.finance.platform.finance.infrastructure.persistence;

import com.finance.platform.finance.domain.InvestmentTransaction;
import com.finance.platform.finance.domain.InvestmentTransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
class InvestmentTransactionRepositoryAdapter implements InvestmentTransactionRepository {

    private final InvestmentTransactionJpaRepository jpaRepository;

    InvestmentTransactionRepositoryAdapter(InvestmentTransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<InvestmentTransaction> findByTransactionIds(Set<UUID> transactionIds) {
        return jpaRepository.findAllByTransactionIdIn(transactionIds).stream().map(this::toDomain).toList();
    }

    private InvestmentTransaction toDomain(InvestmentTransactionJpaEntity e) {
        return new InvestmentTransaction(e.getTransactionId(), e.getAssetId(), e.getQuantity(), e.getPricePerUnit());
    }
}
