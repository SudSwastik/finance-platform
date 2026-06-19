package com.finance.platform.portfolio.web;

import com.finance.platform.portfolio.application.ListHoldingsQuery;
import com.finance.platform.portfolio.application.ListHoldingsQueryHandler;
import com.finance.platform.portfolio.web.dto.HoldingItemDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    private final ListHoldingsQueryHandler queryHandler;
    private final HoldingDtoMapper mapper;

    public PortfolioController(ListHoldingsQueryHandler queryHandler, HoldingDtoMapper mapper) {
        this.queryHandler = queryHandler;
        this.mapper = mapper;
    }

    @GetMapping("/holdings")
    public List<HoldingItemDto> listHoldings() {
        return queryHandler.handle(new ListHoldingsQuery()).stream()
                .map(mapper::toDto)
                .toList();
    }
}
