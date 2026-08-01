package com.finance.platform.security;

import com.finance.platform.common.domain.UserId;
import com.finance.platform.common.tenant.TenantContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * Converts a validated Cognito JWT into a PlatformUserPrincipal. tenant_id/role come from
 * identity.users (via DbUserContextResolver), not JWT claims — used when
 * platform.security.dev-user-sub-enabled=false.
 */
public class JwtTenantAwareConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final DbUserContextResolver userContextResolver;

    /**
     * @param userContextResolver null when this service has no DataSource (e.g. dashboard-bff,
     *                             which only needs the sub, never QueryContext) — falls back to
     *                             OWN scope with no tenant lookup.
     */
    public JwtTenantAwareConverter(DbUserContextResolver userContextResolver) {
        this.userContextResolver = userContextResolver;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String sub = jwt.getSubject();
        TenantContext.set(sub);
        QueryContext.set(resolveScope(sub));

        List<String> groups = QueryContext.require().scope() == QueryContext.Scope.PLATFORM
                ? List.of("ADMIN")
                : List.of("USER");
        PlatformUserPrincipal principal = new PlatformUserPrincipal(UserId.of(sub), groups);
        return new UsernamePasswordAuthenticationToken(principal, jwt, principal.getAuthorities());
    }

    private QueryContext resolveScope(String sub) {
        if (userContextResolver == null) {
            return QueryContext.own(sub);
        }
        return userContextResolver.resolve(sub)
                .filter(ctx -> "ADMIN".equals(ctx.role()))
                .<QueryContext>map(ctx -> QueryContext.platform(sub))
                .orElseGet(() -> QueryContext.own(sub));
    }
}
