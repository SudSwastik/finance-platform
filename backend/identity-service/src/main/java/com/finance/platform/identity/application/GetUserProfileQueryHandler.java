package com.finance.platform.identity.application;

import com.finance.platform.identity.domain.User;
import com.finance.platform.identity.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class GetUserProfileQueryHandler {

    private final UserRepository userRepository;

    public GetUserProfileQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfile handle(GetUserProfileQuery query) {
        User user = userRepository.findByUserSub(query.userSub())
                .orElseThrow(() -> new UserNotFoundException(query.userSub()));
        return new UserProfile(user.id(), user.tenantId(), user.userSub(), user.email());
    }
}
