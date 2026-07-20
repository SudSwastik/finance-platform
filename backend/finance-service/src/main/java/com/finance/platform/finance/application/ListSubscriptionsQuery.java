package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.TransactionFilter;

public record ListSubscriptionsQuery(
        String userSub,
        TransactionFilter filter,
        int page,
        int size
) {}
