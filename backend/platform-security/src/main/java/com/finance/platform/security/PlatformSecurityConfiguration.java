package com.finance.platform.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties(PlatformSecurityProperties.class)
@EnableMethodSecurity
public class PlatformSecurityConfiguration {

    @Bean
    SecurityFilterChain platformSecurityFilterChain(
            HttpSecurity http,
            PlatformSecurityProperties properties,
            ObjectProvider<DbUserContextResolver> userContextResolverProvider) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/health", "/actuator/health", "/error").permitAll()
                        .anyRequest().authenticated());

        // Clear TenantContext at the end of every request (wraps entire chain via try-finally)
        http.addFilterBefore(new TenantContextClearFilter(), UsernamePasswordAuthenticationFilter.class);

        if (properties.isDevUserSubEnabled()) {
            http.addFilterBefore(
                    new DevUserSubAuthenticationFilter(properties),
                    UsernamePasswordAuthenticationFilter.class);
        } else {
            // JWT resource server: spring.security.oauth2.resourceserver.jwt.issuer-uri must be set.
            // No code change is needed when switching from LocalStack to real Cognito — only config.
            // tenant_id/role are resolved from identity.users, not JWT claims (see DbUserContextResolver);
            // resolver is null for services with no DataSource (dashboard-bff), which only need the sub.
            http.oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(
                            new JwtTenantAwareConverter(userContextResolverProvider.getIfAvailable()))));
        }

        return http.build();
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    DbUserContextResolver dbUserContextResolver(DataSource dataSource) {
        return new DbUserContextResolver(dataSource);
    }

    @Bean
    SecurityContextUserIdResolver securityContextUserIdResolver() {
        return new SecurityContextUserIdResolver();
    }

    @Bean
    RestSecurityExceptionHandler restSecurityExceptionHandler() {
        return new RestSecurityExceptionHandler();
    }
}
