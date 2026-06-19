package com.finance.platform.budget.infrastructure.persistence;

import com.finance.platform.budget.domain.BudgetCategory;
import com.finance.platform.budget.domain.BudgetCategoryRepository;
import com.finance.platform.common.domain.Money;
import com.finance.platform.common.tenant.TenantContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BudgetCategoryRepositoryAdapter implements BudgetCategoryRepository {

    private final BudgetCategoryJpaRepository categoryRepository;
    private final UserBudgetSummaryJpaRepository summaryRepository;

    public BudgetCategoryRepositoryAdapter(
            BudgetCategoryJpaRepository categoryRepository,
            UserBudgetSummaryJpaRepository summaryRepository) {
        this.categoryRepository = categoryRepository;
        this.summaryRepository = summaryRepository;
    }

    @Override
    public List<BudgetCategory> findAll() {
        String sub = TenantContext.requireUserSub();
        return categoryRepository.findByUserSubOrderByNameAsc(sub).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Money findTotalDisplay() {
        String sub = TenantContext.requireUserSub();
        return summaryRepository.findById(sub)
                .map(e -> Money.of(e.getTotalDisplay()))
                .orElse(Money.zero());
    }

    private BudgetCategory toDomain(BudgetCategoryJpaEntity entity) {
        return new BudgetCategory(
                entity.getId(),
                entity.getName(),
                entity.getColorToken(),
                Money.of(entity.getSpent()),
                Money.of(entity.getBudgetCap()));
    }
}
