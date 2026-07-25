package com.finance.platform.goals.application;

import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalNotFoundException;
import com.finance.platform.goals.domain.GoalRepository;
import com.finance.platform.goals.domain.InvalidGoalOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateGoalCommandHandler {

    private final GoalRepository repository;

    public UpdateGoalCommandHandler(GoalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Goal handle(UpdateGoalCommand command) {
        if (command.target() != null && command.target().amount().signum() <= 0) {
            throw new InvalidGoalOperationException("Goal target must be a positive amount");
        }
        var goal = repository.findById(command.goalId())
                .orElseThrow(() -> new GoalNotFoundException(command.goalId()));
        if (command.name() != null && !command.name().isBlank()) {
            goal = goal.withName(command.name());
        }
        if (command.target() != null) {
            goal = goal.withTarget(command.target());
        }
        if (command.targetDate() != null) {
            goal = goal.withTargetDate(command.targetDate());
        }
        return repository.save(goal);
    }
}
