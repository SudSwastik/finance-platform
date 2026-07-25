package com.finance.platform.goals.application;

import com.finance.platform.common.domain.Money;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateGoalCommand(UUID goalId, String name, Money target, LocalDate targetDate) {
}
