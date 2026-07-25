package com.finance.platform.goals.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GoalContributionJpaRepository extends JpaRepository<GoalContributionJpaEntity, UUID> {

    List<GoalContributionJpaEntity> findByGoalIdAndUserSubOrderByContributedAtDesc(UUID goalId, String userSub);
}
