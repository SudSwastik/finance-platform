package com.finance.platform.bff;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DashboardBffApplicationTest {
    @Autowired
    private ApplicationContext context;

    @Test
    void startsWithoutDatabaseConfiguration() {
        assertThat(context.getBeansOfType(DataSource.class)).isEmpty();
    }
}
