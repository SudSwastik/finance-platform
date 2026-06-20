package com.finance.platform.finance.domain;

import java.util.List;

public record TransactionPage(
        List<Transaction> content,
        long totalElements,
        int page,
        int size
) {}
