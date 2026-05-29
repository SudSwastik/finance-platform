package com.finance.platform.budget.infrastructure.persistence;

import com.finance.platform.budget.domain.BudgetCategoryRepository;
import com.finance.platform.common.domain.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.finance.platform.budget.BudgetServiceApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.EnabledIfDockerAvailable;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfDockerAvailable
@SpringBootTest(classes = BudgetServiceApplication.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BudgetCategoryRepositoryAdapterIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("finance_dashboard")
            .withUsername("finance")
            .withPassword("finance");

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                postgres.getJdbcUrl() + "?currentSchema=budget");
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "budget");
    }

    @Autowired
    private BudgetCategoryRepository repository;

    @Test
    void findByUserId_loadsSeedCategoriesForAlice() {
        var categories = repository.findByUserId(UserId.of("seed-user-alice"));
        assertEquals(4, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.name().equals("Essentials")));
    }

    @Test
    void findTotalDisplayByUserId_returnsConfiguredTotal() {
        var total = repository.findTotalDisplayByUserId(UserId.of("seed-user-alice"));
        assertEquals("6400", total.toApiString());
    }
}
