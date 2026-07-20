package com.finance.platform.portfolio.web;

import com.finance.platform.portfolio.domain.Holding;
import com.finance.platform.portfolio.web.dto.HoldingItemDto;
import org.springframework.stereotype.Component;

@Component
public class HoldingDtoMapper {

    public HoldingItemDto toDto(Holding holding) {
        return new HoldingItemDto(
                holding.id(),
                holding.symbol(),
                holding.name(),
                holding.assetType().name(),
                holding.costBasis().toApiString(),
                holding.changePercent(),
                holding.currentValue().toApiString());
    }
}
