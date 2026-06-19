package com.finance.platform.finance.infrastructure.persistence;

import com.finance.platform.finance.domain.Account;
import com.finance.platform.finance.domain.AccountRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository jpaRepository;

    AccountRepositoryAdapter(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Account> findAllByUserSub(String userSub) {
        return jpaRepository.findAllByUserSub(userSub).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Account> findById(UUID id, String userSub) {
        return jpaRepository.findByIdAndUserSub(id, userSub).map(this::toDomain);
    }

    @Override
    public void save(Account account) {
        jpaRepository.save(toEntity(account));
    }

    private Account toDomain(AccountJpaEntity e) {
        return new Account(e.getId(), e.getTenantId(), e.getUserSub(), e.getType(), e.getName(), e.getCurrency());
    }

    private AccountJpaEntity toEntity(Account a) {
        AccountJpaEntity e = new AccountJpaEntity();
        e.setId(a.id());
        e.setTenantId(a.tenantId());
        e.setUserSub(a.userSub());
        e.setType(a.type());
        e.setName(a.name());
        e.setCurrency(a.currency());
        return e;
    }
}
