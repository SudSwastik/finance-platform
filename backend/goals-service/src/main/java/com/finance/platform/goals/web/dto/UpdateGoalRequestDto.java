package com.finance.platform.goals.web.dto;

import jakarta.validation.constraints.Pattern;

public record UpdateGoalRequestDto(
        String name,
        @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "target must be a positive decimal amount") String target,
        String targetDate) {
}
