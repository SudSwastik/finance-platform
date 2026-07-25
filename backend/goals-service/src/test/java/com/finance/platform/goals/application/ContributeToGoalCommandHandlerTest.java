package com.finance.platform.goals.application;

import com.finance.platform.common.domain.Money;
import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalContribution;
import com.finance.platform.goals.domain.GoalContributionRepository;
import com.finance.platform.goals.domain.GoalNotFoundException;
import com.finance.platform.goals.domain.GoalRepository;
import com.finance.platform.goals.domain.InvalidGoalOperationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContributeToGoalCommandHandlerTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalContributionRepository contributionRepository;

    @InjectMocks
    private ContributeToGoalCommandHandler handler;

    @Test
    void handle_addsAmountToGoalCurrentAndRecordsContribution() {
        var goalId = UUID.randomUUID();
        var goal = new Goal(goalId, "seed-user-alice", "Emergency Fund", "goal.positive",
                Money.of("15600"), Money.of("20000"), LocalDate.of(2026, 12, 15));

        when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));
        when(contributionRepository.save(any(GoalContribution.class))).thenAnswer(inv -> inv.getArgument(0));
        when(goalRepository.save(any(Goal.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = handler.handle(new ContributeToGoalCommand(goalId, Money.of("500"), "Bonus"));

        assertEquals("16100", result.current().toApiString());
    }

    @Test
    void handle_rejectsNonPositiveAmount() {
        var command = new ContributeToGoalCommand(UUID.randomUUID(), Money.of("0"), null);

        assertThrows(InvalidGoalOperationException.class, () -> handler.handle(command));
    }

    @Test
    void handle_throwsWhenGoalNotFound() {
        var goalId = UUID.randomUUID();
        when(goalRepository.findById(goalId)).thenReturn(Optional.empty());

        assertThrows(GoalNotFoundException.class,
                () -> handler.handle(new ContributeToGoalCommand(goalId, Money.of("100"), null)));
    }
}
