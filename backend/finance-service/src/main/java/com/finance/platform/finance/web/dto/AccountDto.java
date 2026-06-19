package com.finance.platform.finance.web.dto;

import java.util.UUID;

public record AccountDto(UUID id, String type, String name, String currency) {}
