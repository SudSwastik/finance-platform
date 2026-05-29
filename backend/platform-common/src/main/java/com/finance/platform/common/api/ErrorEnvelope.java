package com.finance.platform.common.api;

import java.util.Map;

public record ErrorEnvelope(ErrorBody error) {

    public record ErrorBody(String code, String message, Map<String, Object> details) {

        public static ErrorBody of(String code, String message) {
            return new ErrorBody(code, message, Map.of());
        }
    }

    public static ErrorEnvelope of(String code, String message) {
        return new ErrorEnvelope(ErrorBody.of(code, message));
    }
}
