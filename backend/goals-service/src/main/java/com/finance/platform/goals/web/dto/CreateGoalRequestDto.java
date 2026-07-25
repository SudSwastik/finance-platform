package com.finance.platform.goals.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateGoalRequestDto(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "target must be a positive decimal amount") String target,
        @NotNull String targetDate,
        String colorToken) {
}
