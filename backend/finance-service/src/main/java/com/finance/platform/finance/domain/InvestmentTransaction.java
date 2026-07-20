package com.finance.platform.finance.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record InvestmentTransaction(UUID transactionId, UUID assetId, BigDecimal quantity, BigDecimal pricePerUnit) {}
