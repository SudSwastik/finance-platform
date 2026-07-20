package com.finance.platform.finance.web;

import com.finance.platform.common.tenant.TenantContext;
import com.finance.platform.finance.application.ListTradesQuery;
import com.finance.platform.finance.application.ListTradesQueryHandler;
import com.finance.platform.finance.application.TradeWithDetails;
import com.finance.platform.finance.web.dto.TradeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/trades")
public class TradeController {

    private final ListTradesQueryHandler queryHandler;

    public TradeController(ListTradesQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    @GetMapping
    public List<TradeDto> listTrades() {
        String userSub = TenantContext.requireUserSub();
        return queryHandler.handle(new ListTradesQuery(userSub))
                .stream()
                .map(this::toDto)
                .toList();
    }

    private TradeDto toDto(TradeWithDetails t) {
        return new TradeDto(
                t.transaction().id().toString(),
                t.transaction().type().name(),
                t.asset().symbol(),
                t.asset().name(),
                t.investmentTransaction().quantity().stripTrailingZeros().toPlainString(),
                t.investmentTransaction().pricePerUnit().stripTrailingZeros().toPlainString(),
                t.transaction().amount().toPlainString(),
                t.accountName(),
                t.transaction().transactionDate().toString());
    }
}
