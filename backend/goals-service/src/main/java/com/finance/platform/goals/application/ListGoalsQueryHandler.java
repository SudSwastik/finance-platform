package com.finance.platform.goals.application;

import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListGoalsQueryHandler {

    private final GoalRepository repository;

    public ListGoalsQueryHandler(GoalRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Goal> handle(ListGoalsQuery query) {
        return repository.findAll();
    }
}
