package com.finance.platform.security;

import com.finance.platform.common.domain.UserId;
import com.finance.platform.common.tenant.TenantContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * Converts a validated Cognito JWT into a PlatformUserPrincipal and populates TenantContext.
 * Used when platform.security.dev-user-sub-enabled=false (docker / production profile).
 */
public class JwtTenantAwareConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String sub = jwt.getSubject();
        String tenantId = jwt.getClaimAsString("tenant_id");
        List<String> groups = jwt.getClaimAsStringList("cognito:groups");
        if (groups == null) {
            groups = List.of("USER");
        }
        TenantContext.set(sub);
        QueryContext.set(resolveScope(sub, tenantId, groups));
        PlatformUserPrincipal principal = new PlatformUserPrincipal(UserId.of(sub), groups);
        return new UsernamePasswordAuthenticationToken(principal, jwt, principal.getAuthorities());
    }

    private QueryContext resolveScope(String sub, String tenantId, List<String> groups) {
        if (groups.contains("admin")) {
            return QueryContext.platform(sub);
        }
        if (tenantId != null && !tenantId.isBlank()) {
            return QueryContext.tenant(sub, tenantId);
        }
        return QueryContext.own(sub);
    }
}
