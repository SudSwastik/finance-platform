package com.finance.platform.security;

import com.finance.platform.common.tenant.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class JwtTenantAwareConverterTest {

    @AfterEach
    void clearThreadLocals() {
        TenantContext.clear();
        QueryContext.clear();
    }

    @Test
    void ownScopeWhenNoResolverConfigured() {
        var converter = new JwtTenantAwareConverter(null);

        var principal = (PlatformUserPrincipal) converter.convert(jwt("user-sub-1")).getPrincipal();

        assertThat(principal.userId().value()).isEqualTo("user-sub-1");
        assertThat(TenantContext.getUserSub()).isEqualTo("user-sub-1");
        assertThat(QueryContext.require().scope()).isEqualTo(QueryContext.Scope.OWN);
    }

    @Test
    void ownScopeWhenUserNotYetProvisioned() {
        var resolver = stubResolver(sub -> Optional.empty());

        var result = new JwtTenantAwareConverter(resolver).convert(jwt("brand-new-sub"));

        assertThat(QueryContext.require().scope()).isEqualTo(QueryContext.Scope.OWN);
        assertThat(result.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_USER");
    }

    @Test
    void platformScopeWhenDbRoleIsAdmin() {
        var resolver = stubResolver(sub -> Optional.of(new DbUserContextResolver.UserContext("tenant-1", "ADMIN")));

        var result = new JwtTenantAwareConverter(resolver).convert(jwt("admin-sub"));

        assertThat(QueryContext.require().scope()).isEqualTo(QueryContext.Scope.PLATFORM);
        assertThat(result.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_ADMIN");
    }

    @Test
    void ownScopeWhenDbRoleIsUser() {
        var resolver = stubResolver(sub -> Optional.of(new DbUserContextResolver.UserContext("tenant-1", "USER")));

        var result = new JwtTenantAwareConverter(resolver).convert(jwt("regular-sub"));

        assertThat(QueryContext.require().scope()).isEqualTo(QueryContext.Scope.OWN);
    }

    private static DbUserContextResolver stubResolver(
            java.util.function.Function<String, Optional<DbUserContextResolver.UserContext>> lookup) {
        return new DbUserContextResolver(mock(DataSource.class)) {
            @Override
            public Optional<DbUserContextResolver.UserContext> resolve(String userSub) {
                return lookup.apply(userSub);
            }
        };
    }

    private static Jwt jwt(String subject) {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(subject)
                .claim("sub", subject)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claims(claims -> claims.putAll(Map.of()))
                .build();
    }
}
