package com.finance.platform.goals.application;

import com.finance.platform.goals.domain.GoalContribution;
import com.finance.platform.goals.domain.GoalContributionRepository;
import com.finance.platform.goals.domain.GoalNotFoundException;
import com.finance.platform.goals.domain.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListGoalContributionsQueryHandler {

    private final GoalRepository goalRepository;
    private final GoalContributionRepository contributionRepository;

    public ListGoalContributionsQueryHandler(GoalRepository goalRepository, GoalContributionRepository contributionRepository) {
        this.goalRepository = goalRepository;
        this.contributionRepository = contributionRepository;
    }

    @Transactional(readOnly = true)
    public List<GoalContribution> handle(ListGoalContributionsQuery query) {
        goalRepository.findById(query.goalId())
                .orElseThrow(() -> new GoalNotFoundException(query.goalId()));
        return contributionRepository.findByGoalId(query.goalId());
    }
}
