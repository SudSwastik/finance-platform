package com.finance.platform.bff.client;

import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;
import com.finance.platform.bff.config.PlatformServicesProperties;
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
    public Mono<TotalBudgetsSectionDto> getTotalBudgets(String userSub) {
        return webClient.get()
                .uri("/api/v1/budgets/total-budgets")
                .header("X-Dev-User-Sub", userSub)
                .retrieve()
                .bodyToMono(TotalBudgetsSectionDto.class);
    }
}
