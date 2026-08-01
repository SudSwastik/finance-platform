package com.finance.platform.goals.infrastructure.persistence;

import com.finance.platform.common.domain.Money;
import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GoalRepositoryAdapter implements GoalRepository {

    private final GoalJpaRepository jpaRepository;

    public GoalRepositoryAdapter(GoalJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Goal> findAll() {
        String sub = TenantContext.requireUserSub();
        return jpaRepository.findByUserSubOrderByTargetDateAsc(sub).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Goal> findById(UUID id) {
        String sub = TenantContext.requireUserSub();
        return jpaRepository.findByIdAndUserSub(id, sub).map(this::toDomain);
    }

    @Override
    public Goal save(Goal goal) {
        var entity = new GoalJpaEntity(
                goal.id(),
                TenantDefaults.currentOrPlaceholder(),
                goal.userSub(),
                goal.name(),
                goal.colorToken(),
                goal.current().amount(),
                goal.target().amount(),
                goal.targetDate(),
                goal.version());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        String sub = TenantContext.requireUserSub();
        jpaRepository.deleteByIdAndUserSub(id, sub);
    }

    private Goal toDomain(GoalJpaEntity entity) {
        return new Goal(
                entity.getId(),
                entity.getUserSub(),
                entity.getName(),
                entity.getColorToken(),
                Money.of(entity.getCurrent()),
                Money.of(entity.getTarget()),
                entity.getTargetDate(),
                entity.getVersion());
    }
}
