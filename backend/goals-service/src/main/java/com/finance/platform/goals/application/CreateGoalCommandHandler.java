package com.finance.platform.goals.application;

import com.finance.platform.common.domain.Money;
import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalRepository;
import com.finance.platform.goals.domain.InvalidGoalOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CreateGoalCommandHandler {

    private static final List<String> COLOR_PALETTE = List.of(
            "goal.primary", "goal.positive", "goal.warning", "goal.neutral");

    private final GoalRepository repository;

    public CreateGoalCommandHandler(GoalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Goal handle(CreateGoalCommand command) {
        if (command.target().amount().signum() <= 0) {
            throw new InvalidGoalOperationException("Goal target must be a positive amount");
        }
        String userSub = TenantContext.requireUserSub();
        String colorToken = command.colorToken() != null && !command.colorToken().isBlank()
                ? command.colorToken()
                : COLOR_PALETTE.get(repository.findAll().size() % COLOR_PALETTE.size());
        var goal = new Goal(
                UUID.randomUUID(),
                userSub,
                command.name(),
                colorToken,
                Money.zero(),
                command.target(),
                command.targetDate(),
                0L);
        return repository.save(goal);
    }
}
