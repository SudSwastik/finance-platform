package com.finance.platform.bff.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "platform.security.dev-user-sub-enabled=false")
@AutoConfigureMockMvc
@Import(OverviewJwtIntegrationTest.DownstreamConfiguration.class)
class OverviewJwtIntegrationTest {
    private static final List<ClientRequest> requests = new CopyOnWriteArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void resetRequests() {
        requests.clear();
    }

    @Test
    void authenticatedOverviewForwardsTokenThroughTheRealSecurityChain() throws Exception {
        var jwt = Jwt.withTokenValue("test-access-token").header("alg", "RS256").subject("alice").build();
        when(jwtDecoder.decode("test-access-token")).thenReturn(jwt);

        mockMvc.perform(get("/api/v1/dashboard/overview")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer test-access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBudgets.total").value("0"));

        assertThat(requests).hasSize(5).allSatisfy(request -> {
            assertThat(request.headers().getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer test-access-token");
            assertThat(request.headers().containsKey("X-Dev-User-Sub")).isFalse();
        });
    }

    @Test
    void unauthenticatedOverviewDoesNotCallDownstreamServices() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/overview")).andExpect(status().isUnauthorized());
        assertThat(requests).isEmpty();
    }

    @TestConfiguration
    static class DownstreamConfiguration {
        @Bean
        @Primary
        WebClient.Builder downstreamWebClientBuilder(WebClientConfiguration configuration) {
            return configuration.webClientBuilder().exchangeFunction(request -> {
                requests.add(request);
                if (!"Bearer test-access-token".equals(request.headers().getFirst(HttpHeaders.AUTHORIZATION))) {
                    return Mono.just(ClientResponse.create(HttpStatus.UNAUTHORIZED).build());
                }
                String path = request.url().getPath();
                String body = path.endsWith("total-budgets") ? "{\"total\":\"0\",\"categories\":[]}"
                        : path.endsWith("monthly-summary") ? "{\"income\":\"0\",\"expenses\":\"0\",\"net\":\"0\"}" : "[]";
                return Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json").body(body).build());
            });
        }
    }
}
