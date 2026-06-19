package com.finance.platform.finance.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    List<Account> findAllByUserSub(String userSub);

    Optional<Account> findById(UUID id, String userSub);

    void save(Account account);
}
