package com.finance.platform.finance.application;

import java.time.YearMonth;

public record GetMonthlySummaryQuery(String userSub, YearMonth month) {}
