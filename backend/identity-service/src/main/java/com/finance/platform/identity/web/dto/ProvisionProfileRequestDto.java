package com.finance.platform.identity.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProvisionProfileRequestDto(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "personal|business", message = "must be 'personal' or 'business'") String accountType) {
}
