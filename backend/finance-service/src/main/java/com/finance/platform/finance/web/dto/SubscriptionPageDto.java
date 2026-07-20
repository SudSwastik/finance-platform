package com.finance.platform.finance.web.dto;

import java.util.List;

public record SubscriptionPageDto(
        List<SubscriptionDto> content,
        long totalElements,
        int page,
        int size,
        int totalPages
) {}
