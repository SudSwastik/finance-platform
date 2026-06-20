package com.finance.platform.finance.application;

import java.time.YearMonth;

public record GetTransactionStatsQuery(String userSub, YearMonth month) {}
