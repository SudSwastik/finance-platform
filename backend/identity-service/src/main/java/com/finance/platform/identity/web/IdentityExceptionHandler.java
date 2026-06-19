package com.finance.platform.identity.web;

import com.finance.platform.common.api.ErrorEnvelope;
import com.finance.platform.identity.application.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class IdentityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorEnvelope handleUserNotFound(UserNotFoundException ex) {
        return ErrorEnvelope.of("NOT_FOUND", ex.getMessage());
    }
}
