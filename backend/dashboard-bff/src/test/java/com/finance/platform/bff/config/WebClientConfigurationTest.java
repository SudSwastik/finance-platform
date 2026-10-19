package com.finance.platform.bff.config;

import com.finance.platform.bff.client.WebClientBudgetServiceClient;
import com.finance.platform.bff.client.WebClientFinanceServiceClient;
import com.finance.platform.bff.client.WebClientPortfolioServiceClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WebClientConfigurationTest {
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void forwardsAuthenticatedJwtToEveryOverviewDownstreamRequest() {
        var jwt = Jwt.withTokenValue("test-access-token").header("alg", "RS256").subject("alice").build();
        var authentication = new UsernamePasswordAuthenticationToken("alice", jwt, List.of());
        authentication.eraseCredentials();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer test-access-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        var requests = overviewRequests();

        assertThat(requests).hasSize(5).allSatisfy(request ->
                assertThat(request.headers().getFirst(HttpHeaders.AUTHORIZATION))
                        .isEqualTo("Bearer test-access-token"));
        assertThat(requests).allSatisfy(request ->
                assertThat(request.headers().containsKey("X-Dev-User-Sub")).isFalse());
    }

    @Test
    void retainsDevelopmentAuthenticationWithoutAddingBearerToken() {
        var requests = overviewRequests();

        assertThat(requests).hasSize(5).allSatisfy(request -> {
            assertThat(request.headers().getFirst("X-Dev-User-Sub")).isEqualTo("alice");
            assertThat(request.headers().containsKey(HttpHeaders.AUTHORIZATION)).isFalse();
        });
    }

    private List<ClientRequest> overviewRequests() {
        var requests = new ArrayList<ClientRequest>();
        var builder = new WebClientConfiguration().webClientBuilder().exchangeFunction(request -> {
            requests.add(request);
            String path = request.url().getPath();
            String body = path.endsWith("total-budgets") ? "{\"total\":\"0\",\"categories\":[]}"
                    : path.endsWith("monthly-summary") ? "{\"income\":\"0\",\"expenses\":\"0\",\"net\":\"0\"}" : "[]";
            return Mono.just(ClientResponse.create(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json").body(body).build());
        });
        var properties = new PlatformServicesProperties();
        var budget = new WebClientBudgetServiceClient(builder.clone(), properties);
        var portfolio = new WebClientPortfolioServiceClient(builder.clone(), properties);
        var finance = new WebClientFinanceServiceClient(builder.clone(), properties);
        Mono.zip(budget.getTotalBudgets("alice"), portfolio.getHoldings("alice"),
                finance.getRecurringTransactions("alice"), finance.getRecentTransactions("alice"),
                finance.getMonthlySummary("alice")).block();
        return requests;
    }
}
