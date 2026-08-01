package com.finance.platform.security;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

/**
 * Resolves tenant_id/role for an authenticated sub by querying identity.users directly
 * (same shared Postgres instance as every service, cross-schema read). Keeps tenant_id
 * out of the Cognito JWT entirely — no custom attributes, no Pre-Token-Generation Lambda.
 */
public class DbUserContextResolver {

    public record UserContext(String tenantId, String role) {}

    private final JdbcTemplate jdbcTemplate;

    public DbUserContextResolver(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<UserContext> resolve(String userSub) {
        return jdbcTemplate.query(
                        "SELECT tenant_id, role FROM identity.users WHERE user_sub = ?",
                        (rs, rowNum) -> new UserContext(rs.getString("tenant_id"), rs.getString("role")),
                        userSub)
                .stream()
                .findFirst();
    }
}
