package com.finance.platform.budget.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBudgetSummaryJpaRepository extends JpaRepository<UserBudgetSummaryJpaEntity, String> {
}
