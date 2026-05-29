package com.finance.platform.bff.web.dto;

import java.util.List;

public record MeResponseDto(String sub, List<String> groups) {
}
