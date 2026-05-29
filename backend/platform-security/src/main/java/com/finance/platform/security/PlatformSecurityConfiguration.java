package com.finance.platform.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties(PlatformSecurityProperties.class)
@EnableMethodSecurity
public class PlatformSecurityConfiguration {

    @Bean
    SecurityFilterChain platformSecurityFilterChain(
            HttpSecurity http,
            PlatformSecurityProperties properties) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/health", "/actuator/health").permitAll()
                        .anyRequest().authenticated());

        if (properties.isDevUserSubEnabled()) {
            http.addFilterBefore(
                    new DevUserSubAuthenticationFilter(properties),
                    UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
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
