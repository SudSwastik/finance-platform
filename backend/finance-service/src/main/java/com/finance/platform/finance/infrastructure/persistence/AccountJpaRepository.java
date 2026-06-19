package com.finance.platform.finance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, UUID> {

    List<AccountJpaEntity> findAllByUserSub(String userSub);

    Optional<AccountJpaEntity> findByIdAndUserSub(UUID id, String userSub);
}
