package com.finance.platform.identity.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUserSub(String userSub);

    void save(User user);
}
