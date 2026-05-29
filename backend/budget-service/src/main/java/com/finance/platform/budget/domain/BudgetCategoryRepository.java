package com.finance.platform.budget.domain;

import com.finance.platform.common.domain.Money;
import com.finance.platform.common.domain.UserId;

import java.util.List;

public interface BudgetCategoryRepository {

    List<BudgetCategory> findByUserId(UserId userId);

    Money findTotalDisplayByUserId(UserId userId);
}
