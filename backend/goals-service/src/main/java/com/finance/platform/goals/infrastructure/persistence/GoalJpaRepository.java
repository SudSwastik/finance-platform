package com.finance.platform.goals.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalJpaRepository extends JpaRepository<GoalJpaEntity, UUID> {

    List<GoalJpaEntity> findByUserSubOrderByTargetDateAsc(String userSub);

    Optional<GoalJpaEntity> findByIdAndUserSub(UUID id, String userSub);

    void deleteByIdAndUserSub(UUID id, String userSub);
}
