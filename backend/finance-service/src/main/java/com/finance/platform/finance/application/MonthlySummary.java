package com.finance.platform.finance.application;

import java.math.BigDecimal;

public record MonthlySummary(BigDecimal income, BigDecimal spending, BigDecimal netSaved) {}
