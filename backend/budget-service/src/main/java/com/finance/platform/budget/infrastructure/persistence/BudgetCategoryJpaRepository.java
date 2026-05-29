package com.finance.platform.budget.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetCategoryJpaRepository extends JpaRepository<BudgetCategoryJpaEntity, UUID> {

    List<BudgetCategoryJpaEntity> findByUserSubOrderByNameAsc(String userSub);
}
