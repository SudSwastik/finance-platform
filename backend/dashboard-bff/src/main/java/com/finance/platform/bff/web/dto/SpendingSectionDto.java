package com.finance.platform.bff.web.dto;

import java.time.LocalDate;
import java.util.List;

public record SpendingSectionDto(String currentTotal, String comparisonLabel, List<SpendingPointDto> series) {

    public record SpendingPointDto(LocalDate date, String thisMonth, String lastMonth) {
    }
}
