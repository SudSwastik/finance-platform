package com.finance.platform.goals.application;

import com.finance.platform.common.domain.Money;

import java.util.UUID;

public record ContributeToGoalCommand(UUID goalId, Money amount, String note) {
}
