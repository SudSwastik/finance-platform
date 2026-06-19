package com.finance.platform.identity.infrastructure.persistence;

import com.finance.platform.identity.domain.User;
import com.finance.platform.identity.domain.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByUserSub(String userSub) {
        return jpaRepository.findByUserSub(userSub).map(this::toDomain);
    }

    @Override
    public void save(User user) {
        jpaRepository.save(toEntity(user));
    }

    private User toDomain(UserJpaEntity e) {
        return new User(e.getId(), e.getTenantId(), e.getUserSub(), e.getEmail());
    }

    private UserJpaEntity toEntity(User u) {
        UserJpaEntity e = new UserJpaEntity();
        e.setId(u.id());
        e.setTenantId(u.tenantId());
        e.setUserSub(u.userSub());
        e.setEmail(u.email());
        return e;
    }
}
