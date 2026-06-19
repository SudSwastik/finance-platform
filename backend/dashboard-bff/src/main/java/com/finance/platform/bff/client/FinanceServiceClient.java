package com.finance.platform.bff.client;

import com.finance.platform.bff.web.dto.RecurringItemDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FinanceServiceClient {

    Mono<List<RecurringItemDto>> getRecurringTransactions(String userSub);
}
