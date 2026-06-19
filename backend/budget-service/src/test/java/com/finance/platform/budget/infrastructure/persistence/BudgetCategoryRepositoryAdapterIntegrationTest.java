package com.finance.platform.budget.infrastructure.persistence;

import com.finance.platform.budget.domain.BudgetCategoryRepository;
import com.finance.platform.common.tenant.TenantContext;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void clearTenant() {
        TenantContext.clear();
    }

    @Test
    void findAll_loadsSeedCategoriesForAlice() {
        TenantContext.set("seed-user-alice");
        var categories = repository.findAll();
        assertEquals(4, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.name().equals("Essentials")));
    }

    @Test
    void findTotalDisplay_returnsConfiguredTotal() {
        TenantContext.set("seed-user-alice");
        var total = repository.findTotalDisplay();
        assertEquals("6400", total.toApiString());
    }
}
