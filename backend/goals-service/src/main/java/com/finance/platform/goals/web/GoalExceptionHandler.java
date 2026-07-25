package com.finance.platform.goals.web;

import com.finance.platform.common.api.ErrorEnvelope;
import com.finance.platform.goals.domain.GoalNotFoundException;
import com.finance.platform.goals.domain.InvalidGoalOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GoalExceptionHandler {

    @ExceptionHandler(GoalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorEnvelope handleGoalNotFound(GoalNotFoundException ex) {
        return ErrorEnvelope.of("NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(InvalidGoalOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorEnvelope handleInvalidOperation(InvalidGoalOperationException ex) {
        return ErrorEnvelope.of("INVALID_OPERATION", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorEnvelope handleValidation(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        return ErrorEnvelope.of("VALIDATION_ERROR", message);
    }
}
