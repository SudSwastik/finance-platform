package com.finance.platform.goals.application;

import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalContribution;
import com.finance.platform.goals.domain.GoalContributionRepository;
import com.finance.platform.goals.domain.GoalNotFoundException;
import com.finance.platform.goals.domain.GoalRepository;
import com.finance.platform.goals.domain.InvalidGoalOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class ContributeToGoalCommandHandler {

    private final GoalRepository goalRepository;
    private final GoalContributionRepository contributionRepository;

    public ContributeToGoalCommandHandler(GoalRepository goalRepository, GoalContributionRepository contributionRepository) {
        this.goalRepository = goalRepository;
        this.contributionRepository = contributionRepository;
    }

    @Transactional
    public Goal handle(ContributeToGoalCommand command) {
        if (command.amount().amount().signum() <= 0) {
            throw new InvalidGoalOperationException("Contribution amount must be positive");
        }
        var goal = goalRepository.findById(command.goalId())
                .orElseThrow(() -> new GoalNotFoundException(command.goalId()));

        var contribution = new GoalContribution(
                UUID.randomUUID(),
                goal.id(),
                goal.userSub(),
                command.amount(),
                command.note(),
                Instant.now());
        contributionRepository.save(contribution);

        return goalRepository.save(goal.withContribution(command.amount()));
    }
}
