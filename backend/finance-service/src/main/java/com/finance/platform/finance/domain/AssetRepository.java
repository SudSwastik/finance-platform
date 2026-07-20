package com.finance.platform.finance.domain;

import java.util.List;

public interface AssetRepository {

    List<Asset> findAll();
}
