package com.finance.platform.goals.infrastructure.persistence;

import com.finance.platform.common.domain.Money;
import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.goals.GoalsServiceApplication;
import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.EnabledIfDockerAvailable;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfDockerAvailable
@SpringBootTest(classes = GoalsServiceApplication.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GoalRepositoryAdapterIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("finance_dashboard")
            .withUsername("finance")
            .withPassword("finance");

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                postgres.getJdbcUrl() + "?currentSchema=goals");
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "goals");
    }

    @Autowired
    private GoalRepository repository;

    @AfterEach
    void clearTenant() {
        TenantContext.clear();
    }

    @Test
    void findAll_loadsSeedGoalsForAlice() {
        TenantContext.set("seed-user-alice");
        var goals = repository.findAll();
        assertEquals(3, goals.size());
        assertTrue(goals.stream().anyMatch(g -> g.name().equals("Emergency Fund")));
    }

    @Test
    void save_persistsNewGoalAndFindByIdReturnsIt() {
        TenantContext.set("seed-user-alice");
        var goal = new Goal(UUID.randomUUID(), "seed-user-alice", "New Car", "goal.warning",
                Money.zero(), Money.of("30000"), LocalDate.of(2027, 1, 1), 0L);

        var saved = repository.save(goal);
        var found = repository.findById(saved.id());

        assertTrue(found.isPresent());
        assertEquals("New Car", found.get().name());
    }

    @Test
    void save_withStaleVersion_throwsOptimisticLockingFailure() {
        TenantContext.set("seed-user-alice");
        var created = repository.save(new Goal(UUID.randomUUID(), "seed-user-alice", "Bike", "goal.warning",
                Money.zero(), Money.of("2000"), LocalDate.of(2027, 6, 1), 0L));

        var staleCopy = repository.findById(created.id()).orElseThrow();

        // First writer succeeds and bumps the version.
        repository.save(staleCopy.withName("Bike (renamed)"));

        // Second writer still holds the pre-update version -> must be rejected, not silently overwritten.
        assertThrows(OptimisticLockingFailureException.class,
                () -> repository.save(staleCopy.withName("Bike (conflicting rename)")));
    }

    @Test
    void findById_scopedToOwningUser_isEmptyForOtherUser() {
        TenantContext.set("seed-user-alice");
        var goals = repository.findAll();
        var aliceGoalId = goals.getFirst().id();

        TenantContext.set("seed-user-bob");
        var found = repository.findById(aliceGoalId);

        assertTrue(found.isEmpty());
    }
}
