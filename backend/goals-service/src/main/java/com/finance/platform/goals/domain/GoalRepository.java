package com.finance.platform.goals.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository {

    List<Goal> findAll();

    Optional<Goal> findById(UUID id);

    Goal save(Goal goal);

    void deleteById(UUID id);
}
