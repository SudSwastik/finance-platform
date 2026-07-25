package com.finance.platform.goals.application;

import com.finance.platform.goals.domain.GoalNotFoundException;
import com.finance.platform.goals.domain.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteGoalCommandHandler {

    private final GoalRepository repository;

    public DeleteGoalCommandHandler(GoalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handle(DeleteGoalCommand command) {
        repository.findById(command.goalId())
                .orElseThrow(() -> new GoalNotFoundException(command.goalId()));
        repository.deleteById(command.goalId());
    }
}
