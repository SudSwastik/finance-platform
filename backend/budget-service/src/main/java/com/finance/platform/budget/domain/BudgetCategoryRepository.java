package com.finance.platform.budget.domain;

import com.finance.platform.common.domain.Money;

import java.util.List;

public interface BudgetCategoryRepository {

    List<BudgetCategory> findAll();

    Money findTotalDisplay();
}
