package com.finance.platform.finance.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    List<Transaction> findAllByUserSub(String userSub);

    List<Transaction> findRecurringByUserSub(String userSub);

    List<Transaction> findRecentByUserSub(String userSub);

    List<Transaction> findByUserSubAndDateBetween(String userSub, LocalDate from, LocalDate to);

    List<Transaction> findByAccountId(UUID accountId, String userSub);

    TransactionPage findPage(String userSub, TransactionFilter filter, int page, int size);

    void save(Transaction transaction);
}
