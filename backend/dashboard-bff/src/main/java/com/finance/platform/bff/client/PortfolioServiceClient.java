package com.finance.platform.bff.client;

import com.finance.platform.bff.web.dto.HoldingItemDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PortfolioServiceClient {
    Mono<List<HoldingItemDto>> getHoldings(String userSub);
}
