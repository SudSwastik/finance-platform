package com.finance.platform.bff.client;

import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;
import com.finance.platform.bff.config.PlatformServicesProperties;
import com.finance.platform.common.domain.UserId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientBudgetServiceClient implements BudgetServiceClient {

    private final WebClient webClient;

    public WebClientBudgetServiceClient(WebClient.Builder builder, PlatformServicesProperties properties) {
        this.webClient = builder.baseUrl(properties.getBudget()).build();
    }

    @Override
    public Mono<TotalBudgetsSectionDto> getTotalBudgets(UserId userId, String devUserSubHeaderValue) {
        return webClient.get()
                .uri("/api/v1/budgets/total-budgets")
                .header("X-Dev-User-Sub", devUserSubHeaderValue)
                .retrieve()
                .bodyToMono(TotalBudgetsSectionDto.class);
    }
}
