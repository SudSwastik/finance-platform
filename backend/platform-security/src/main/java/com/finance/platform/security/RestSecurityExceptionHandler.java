package com.finance.platform.security;

import com.finance.platform.common.api.ErrorEnvelope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestSecurityExceptionHandler {

    @ExceptionHandler(UnauthorizedUserException.class)
    ResponseEntity<ErrorEnvelope> unauthorized(UnauthorizedUserException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorEnvelope.of("UNAUTHORIZED", ex.getMessage()));
    }
}
