package com.finance.platform.bff.web.dto;

import java.math.BigDecimal;

public record HoldingItemDto(
        String id,
        String symbol,
        String name,
        String costBasis,
        BigDecimal changePercent,
        String value) {
}
