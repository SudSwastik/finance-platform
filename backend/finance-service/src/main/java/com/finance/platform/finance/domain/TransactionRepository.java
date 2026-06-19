package com.finance.platform.finance.domain;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    List<Transaction> findAllByUserSub(String userSub);

    List<Transaction> findRecurringByUserSub(String userSub);

    List<Transaction> findByAccountId(UUID accountId, String userSub);

    void save(Transaction transaction);
}
