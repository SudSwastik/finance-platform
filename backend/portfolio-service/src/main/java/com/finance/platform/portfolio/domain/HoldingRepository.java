package com.finance.platform.portfolio.domain;

import java.util.List;

public interface HoldingRepository {
    List<Holding> findAll();
}
