package com.finance.platform.bff.client;

import com.finance.platform.bff.config.PlatformServicesProperties;
import com.finance.platform.bff.web.dto.HoldingItemDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class WebClientPortfolioServiceClient implements PortfolioServiceClient {

    private final WebClient webClient;

    public WebClientPortfolioServiceClient(WebClient.Builder builder, PlatformServicesProperties properties) {
        this.webClient = builder.baseUrl(properties.getPortfolio()).build();
    }

    @Override
    public Mono<List<HoldingItemDto>> getHoldings(String userSub) {
        return webClient.get()
                .uri("/api/v1/portfolio/holdings")
                .header("X-Dev-User-Sub", userSub)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<HoldingItemDto>>() {});
    }
}
