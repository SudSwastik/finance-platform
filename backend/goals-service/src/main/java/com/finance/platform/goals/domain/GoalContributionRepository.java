package com.finance.platform.goals.domain;

import java.util.List;
import java.util.UUID;

public interface GoalContributionRepository {

    List<GoalContribution> findByGoalId(UUID goalId);

    GoalContribution save(GoalContribution contribution);
}
