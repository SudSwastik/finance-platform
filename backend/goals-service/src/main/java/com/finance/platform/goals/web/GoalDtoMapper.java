package com.finance.platform.goals.web;

import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalContribution;
import com.finance.platform.goals.web.dto.GoalContributionDto;
import com.finance.platform.goals.web.dto.GoalItemDto;
import org.springframework.stereotype.Component;

@Component
public class GoalDtoMapper {

    public GoalItemDto toDto(Goal goal) {
        return new GoalItemDto(
                goal.id().toString(),
                goal.name(),
                goal.current().toApiString(),
                goal.target().toApiString(),
                goal.percent(),
                goal.colorToken(),
                goal.targetDate().toString());
    }

    public GoalContributionDto toDto(GoalContribution contribution) {
        return new GoalContributionDto(
                contribution.id().toString(),
                contribution.goalId().toString(),
                contribution.amount().toApiString(),
                contribution.note(),
                contribution.contributedAt().toString());
    }
}
