package com.finance.platform.goals.web;

import com.finance.platform.common.domain.Money;
import com.finance.platform.goals.application.ContributeToGoalCommand;
import com.finance.platform.goals.application.ContributeToGoalCommandHandler;
import com.finance.platform.goals.application.CreateGoalCommand;
import com.finance.platform.goals.application.CreateGoalCommandHandler;
import com.finance.platform.goals.application.DeleteGoalCommand;
import com.finance.platform.goals.application.DeleteGoalCommandHandler;
import com.finance.platform.goals.application.ListGoalContributionsQuery;
import com.finance.platform.goals.application.ListGoalContributionsQueryHandler;
import com.finance.platform.goals.application.ListGoalsQuery;
import com.finance.platform.goals.application.ListGoalsQueryHandler;
import com.finance.platform.goals.application.UpdateGoalCommand;
import com.finance.platform.goals.application.UpdateGoalCommandHandler;
import com.finance.platform.goals.web.dto.ContributeToGoalRequestDto;
import com.finance.platform.goals.web.dto.CreateGoalRequestDto;
import com.finance.platform.goals.web.dto.GoalContributionDto;
import com.finance.platform.goals.web.dto.GoalItemDto;
import com.finance.platform.goals.web.dto.UpdateGoalRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goals")
public class GoalController {

    private final ListGoalsQueryHandler listGoalsQueryHandler;
    private final CreateGoalCommandHandler createGoalCommandHandler;
    private final UpdateGoalCommandHandler updateGoalCommandHandler;
    private final DeleteGoalCommandHandler deleteGoalCommandHandler;
    private final ListGoalContributionsQueryHandler listGoalContributionsQueryHandler;
    private final ContributeToGoalCommandHandler contributeToGoalCommandHandler;
    private final GoalDtoMapper mapper;

    public GoalController(
            ListGoalsQueryHandler listGoalsQueryHandler,
            CreateGoalCommandHandler createGoalCommandHandler,
            UpdateGoalCommandHandler updateGoalCommandHandler,
            DeleteGoalCommandHandler deleteGoalCommandHandler,
            ListGoalContributionsQueryHandler listGoalContributionsQueryHandler,
            ContributeToGoalCommandHandler contributeToGoalCommandHandler,
            GoalDtoMapper mapper) {
        this.listGoalsQueryHandler = listGoalsQueryHandler;
        this.createGoalCommandHandler = createGoalCommandHandler;
        this.updateGoalCommandHandler = updateGoalCommandHandler;
        this.deleteGoalCommandHandler = deleteGoalCommandHandler;
        this.listGoalContributionsQueryHandler = listGoalContributionsQueryHandler;
        this.contributeToGoalCommandHandler = contributeToGoalCommandHandler;
        this.mapper = mapper;
    }

    @GetMapping
    public List<GoalItemDto> listGoals() {
        return listGoalsQueryHandler.handle(new ListGoalsQuery()).stream()
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<GoalItemDto> createGoal(@Valid @RequestBody CreateGoalRequestDto request) {
        var command = new CreateGoalCommand(
                request.name(),
                Money.of(request.target()),
                LocalDate.parse(request.targetDate()),
                request.colorToken());
        var goal = createGoalCommandHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(goal));
    }

    @PatchMapping("/{id}")
    public GoalItemDto updateGoal(@PathVariable UUID id, @Valid @RequestBody UpdateGoalRequestDto request) {
        var command = new UpdateGoalCommand(
                id,
                request.name(),
                request.target() != null ? Money.of(request.target()) : null,
                request.targetDate() != null ? LocalDate.parse(request.targetDate()) : null);
        return mapper.toDto(updateGoalCommandHandler.handle(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID id) {
        deleteGoalCommandHandler.handle(new DeleteGoalCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/contributions")
    public List<GoalContributionDto> listGoalContributions(@PathVariable UUID id) {
        return listGoalContributionsQueryHandler.handle(new ListGoalContributionsQuery(id)).stream()
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping("/{id}/contributions")
    public ResponseEntity<GoalItemDto> contributeToGoal(@PathVariable UUID id, @Valid @RequestBody ContributeToGoalRequestDto request) {
        var command = new ContributeToGoalCommand(id, Money.of(request.amount()), request.note());
        var goal = contributeToGoalCommandHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(goal));
    }
}
