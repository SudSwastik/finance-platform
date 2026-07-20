package com.finance.platform.finance.web.dto;

public record TradeDto(
        String id,
        String side,
        String assetSymbol,
        String assetName,
        String quantity,
        String pricePerUnit,
        String amount,
        String accountName,
        String tradeDate
) {}
