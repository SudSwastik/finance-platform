package com.finance.platform.security;

import com.finance.platform.common.domain.UserId;
import com.finance.platform.common.tenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Local-dev authentication via {@code X-Dev-User-Sub}. Replaced by Cognito JWT in production profile.
 */
public class DevUserSubAuthenticationFilter extends OncePerRequestFilter {

    private final PlatformSecurityProperties properties;

    public DevUserSubAuthenticationFilter(PlatformSecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String sub = request.getHeader(properties.getDevUserSubHeader());
            if (sub != null && !sub.isBlank()) {
                String trimmed = sub.trim();
                PlatformUserPrincipal principal = new PlatformUserPrincipal(UserId.of(trimmed), List.of("USER"));
                var authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                TenantContext.set(trimmed);
                QueryContext.set(QueryContext.own(trimmed));
            }
        }
        filterChain.doFilter(request, response);
    }
}
