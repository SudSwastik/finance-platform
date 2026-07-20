package com.finance.platform.finance.domain;

import java.util.UUID;

public record Asset(UUID id, String symbol, String name, AssetType assetType) {}
