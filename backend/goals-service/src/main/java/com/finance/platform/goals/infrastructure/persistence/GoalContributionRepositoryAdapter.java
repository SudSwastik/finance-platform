package com.finance.platform.goals.infrastructure.persistence;

import com.finance.platform.common.domain.Money;
import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.goals.domain.GoalContribution;
import com.finance.platform.goals.domain.GoalContributionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class GoalContributionRepositoryAdapter implements GoalContributionRepository {

    private final GoalContributionJpaRepository jpaRepository;

    public GoalContributionRepositoryAdapter(GoalContributionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<GoalContribution> findByGoalId(UUID goalId) {
        String sub = TenantContext.requireUserSub();
        return jpaRepository.findByGoalIdAndUserSubOrderByContributedAtDesc(goalId, sub).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public GoalContribution save(GoalContribution contribution) {
        var entity = new GoalContributionJpaEntity(
                contribution.id(),
                contribution.goalId(),
                TenantDefaults.currentOrPlaceholder(),
                contribution.userSub(),
                contribution.amount().amount(),
                contribution.note(),
                contribution.contributedAt());
        return toDomain(jpaRepository.save(entity));
    }

    private GoalContribution toDomain(GoalContributionJpaEntity entity) {
        return new GoalContribution(
                entity.getId(),
                entity.getGoalId(),
                entity.getUserSub(),
                Money.of(entity.getAmount()),
                entity.getNote(),
                entity.getContributedAt());
    }
}
