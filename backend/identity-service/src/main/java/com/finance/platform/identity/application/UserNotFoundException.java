package com.finance.platform.identity.application;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userSub) {
        super("User not found: " + userSub);
    }
}
