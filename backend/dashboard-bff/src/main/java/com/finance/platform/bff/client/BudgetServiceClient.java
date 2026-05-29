package com.finance.platform.bff.client;

import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;
import com.finance.platform.common.domain.UserId;
import reactor.core.publisher.Mono;

public interface BudgetServiceClient {

    Mono<TotalBudgetsSectionDto> getTotalBudgets(UserId userId, String devUserSubHeaderValue);
}
