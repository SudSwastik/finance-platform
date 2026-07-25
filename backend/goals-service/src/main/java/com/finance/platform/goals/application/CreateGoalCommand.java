package com.finance.platform.goals.application;

import com.finance.platform.common.domain.Money;

import java.time.LocalDate;

public record CreateGoalCommand(String name, Money target, LocalDate targetDate, String colorToken) {
}
