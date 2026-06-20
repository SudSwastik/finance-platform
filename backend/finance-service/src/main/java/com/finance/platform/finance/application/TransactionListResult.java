package com.finance.platform.finance.application;

import java.util.List;

public record TransactionListResult(
        List<TransactionWithAccount> content,
        long totalElements,
        int page,
        int size
) {}
