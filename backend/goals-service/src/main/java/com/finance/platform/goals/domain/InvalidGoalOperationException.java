package com.finance.platform.goals.domain;

public class InvalidGoalOperationException extends RuntimeException {

    public InvalidGoalOperationException(String message) {
        super(message);
    }
}
