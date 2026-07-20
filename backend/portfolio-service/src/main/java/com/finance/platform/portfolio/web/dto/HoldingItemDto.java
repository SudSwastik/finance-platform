package com.finance.platform.portfolio.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record HoldingItemDto(
        UUID id, String symbol, String name, String assetType,
        String costBasis, BigDecimal changePercent, String value) {}
