package com.finance.platform.bff.client;

import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;
import reactor.core.publisher.Mono;

public interface BudgetServiceClient {

    Mono<TotalBudgetsSectionDto> getTotalBudgets(String userSub);
}
