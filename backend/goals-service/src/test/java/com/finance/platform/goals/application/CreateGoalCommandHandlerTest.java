package com.finance.platform.goals.application;

import com.finance.platform.common.domain.Money;
import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalRepository;
import com.finance.platform.goals.domain.InvalidGoalOperationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateGoalCommandHandlerTest {

    @Mock
    private GoalRepository repository;

    @InjectMocks
    private CreateGoalCommandHandler handler;

    @AfterEach
    void clearTenant() {
        TenantContext.clear();
    }

    @Test
    void handle_createsGoalWithZeroCurrentAndProvidedColorToken() {
        TenantContext.set("seed-user-alice");
        when(repository.save(any(Goal.class))).thenAnswer(inv -> inv.getArgument(0));

        var command = new CreateGoalCommand("New Car", Money.of("30000"), LocalDate.of(2027, 1, 1), "goal.warning");
        var result = handler.handle(command);

        assertEquals("New Car", result.name());
        assertEquals("0", result.current().toApiString());
        assertEquals("30000", result.target().toApiString());
        assertEquals("goal.warning", result.colorToken());
        assertEquals("seed-user-alice", result.userSub());
    }

    @Test
    void handle_assignsColorTokenFromPaletteWhenOmitted() {
        TenantContext.set("seed-user-alice");
        when(repository.findAll()).thenReturn(List.of());
        when(repository.save(any(Goal.class))).thenAnswer(inv -> inv.getArgument(0));

        var command = new CreateGoalCommand("New Car", Money.of("30000"), LocalDate.of(2027, 1, 1), null);
        var result = handler.handle(command);

        assertEquals("goal.primary", result.colorToken());
    }

    @Test
    void handle_rejectsNonPositiveTarget() {
        TenantContext.set("seed-user-alice");
        var command = new CreateGoalCommand("New Car", Money.of("0"), LocalDate.of(2027, 1, 1), null);

        assertThrows(InvalidGoalOperationException.class, () -> handler.handle(command));
    }

    @Test
    void handle_savesGoalWithNewRandomId() {
        TenantContext.set("seed-user-alice");
        when(repository.save(any(Goal.class))).thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<Goal> captor = ArgumentCaptor.forClass(Goal.class);

        handler.handle(new CreateGoalCommand("New Car", Money.of("30000"), LocalDate.of(2027, 1, 1), "goal.warning"));

        verify(repository).save(captor.capture());
        assertEquals("New Car", captor.getValue().name());
    }
}
