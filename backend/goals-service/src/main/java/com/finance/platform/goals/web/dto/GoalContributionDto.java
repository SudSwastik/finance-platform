package com.finance.platform.goals.web.dto;

public record GoalContributionDto(
        String id,
        String goalId,
        String amount,
        String note,
        String contributedAt) {
}
