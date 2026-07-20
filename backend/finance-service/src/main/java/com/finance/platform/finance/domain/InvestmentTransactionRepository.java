package com.finance.platform.finance.domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface InvestmentTransactionRepository {

    List<InvestmentTransaction> findByTransactionIds(Set<UUID> transactionIds);
}
