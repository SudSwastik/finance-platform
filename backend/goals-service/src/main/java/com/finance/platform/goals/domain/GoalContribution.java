package com.finance.platform.goals.domain;

import com.finance.platform.common.domain.Money;

import java.time.Instant;
import java.util.UUID;

public record GoalContribution(UUID id, UUID goalId, String userSub, Money amount, String note, Instant contributedAt) {
}
