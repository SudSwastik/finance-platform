package com.finance.platform.portfolio.application;

import com.finance.platform.portfolio.domain.Holding;
import com.finance.platform.portfolio.domain.HoldingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListHoldingsQueryHandler {

    private final HoldingRepository repository;

    public ListHoldingsQueryHandler(HoldingRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Holding> handle(ListHoldingsQuery query) {
        return repository.findAll();
    }
}
