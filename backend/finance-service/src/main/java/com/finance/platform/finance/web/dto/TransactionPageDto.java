package com.finance.platform.finance.web.dto;

import java.util.List;

public record TransactionPageDto(
        List<TransactionDto> content,
        long totalElements,
        int page,
        int size,
        int totalPages
) {}
