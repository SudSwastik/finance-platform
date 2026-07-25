package com.finance.platform.goals.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ContributeToGoalRequestDto(
        @NotBlank @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "amount must be a positive decimal amount") String amount,
        String note) {
}
