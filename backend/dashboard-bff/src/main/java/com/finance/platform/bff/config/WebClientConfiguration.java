package com.finance.platform.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder().defaultRequest(request -> {
            // Capture the validated request token before reactive work changes threads.
            // ProviderManager erases credentials from our authentication token after validation.
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
                var token = new DefaultBearerTokenResolver().resolve(attributes.getRequest());
                if (token != null) {
                    request.headers(headers -> {
                        headers.setBearerAuth(token);
                        headers.remove("X-Dev-User-Sub");
                    });
                }
            }
        });
    }
}
