package com.finance.platform.bff.client;

import com.finance.platform.bff.config.PlatformServicesProperties;
import com.finance.platform.bff.web.dto.RecurringItemDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class WebClientFinanceServiceClient implements FinanceServiceClient {

    private final WebClient webClient;

    public WebClientFinanceServiceClient(WebClient.Builder builder, PlatformServicesProperties properties) {
        this.webClient = builder.baseUrl(properties.getFinance()).build();
    }

    @Override
    public Mono<List<RecurringItemDto>> getRecurringTransactions(String userSub) {
        return webClient.get()
                .uri("/api/v1/finance/transactions/recurring")
                .header("X-Dev-User-Sub", userSub)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RecurringItemDto>>() {});
    }
}
