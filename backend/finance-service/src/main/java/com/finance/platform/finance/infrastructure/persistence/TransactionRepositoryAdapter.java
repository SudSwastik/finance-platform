package com.finance.platform.finance.infrastructure.persistence;

import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    TransactionRepositoryAdapter(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Transaction> findAllByUserSub(String userSub) {
        return jpaRepository.findAllByUserSub(userSub).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Transaction> findRecurringByUserSub(String userSub) {
        return jpaRepository.findAllByUserSubAndRecurringTrue(userSub).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Transaction> findRecentByUserSub(String userSub) {
        return jpaRepository.findTop10ByUserSubOrderByTransactionDateDesc(userSub).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Transaction> findByUserSubAndDateBetween(String userSub, LocalDate from, LocalDate to) {
        return jpaRepository.findAllByUserSubAndTransactionDateBetween(userSub, from, to).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId, String userSub) {
        return jpaRepository.findAllByAccountIdAndUserSub(accountId, userSub).stream().map(this::toDomain).toList();
    }

    @Override
    public void save(Transaction transaction) {
        jpaRepository.save(toEntity(transaction));
    }

    private Transaction toDomain(TransactionJpaEntity e) {
        return new Transaction(
                e.getId(), e.getTenantId(), e.getUserSub(), e.getAccountId(),
                e.getAmount(), e.getCurrency(), e.getType(), e.getStatus(),
                e.getMerchantName(), e.getCategory(), e.getDescription(),
                e.isRecurring(), e.getTransactionDate());
    }

    private TransactionJpaEntity toEntity(Transaction t) {
        TransactionJpaEntity e = new TransactionJpaEntity();
        e.setId(t.id());
        e.setTenantId(t.tenantId());
        e.setUserSub(t.userSub());
        e.setAccountId(t.accountId());
        e.setAmount(t.amount());
        e.setCurrency(t.currency());
        e.setType(t.type());
        e.setStatus(t.status());
        e.setMerchantName(t.merchantName());
        e.setCategory(t.category());
        e.setDescription(t.description());
        e.setRecurring(t.isRecurring());
        e.setTransactionDate(t.transactionDate());
        e.setCreatedBy(t.userSub());
        return e;
    }
}
