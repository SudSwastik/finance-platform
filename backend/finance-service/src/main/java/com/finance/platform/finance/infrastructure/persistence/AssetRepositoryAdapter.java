package com.finance.platform.finance.infrastructure.persistence;

import com.finance.platform.finance.domain.Asset;
import com.finance.platform.finance.domain.AssetRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class AssetRepositoryAdapter implements AssetRepository {

    private final AssetJpaRepository jpaRepository;

    AssetRepositoryAdapter(AssetJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Asset> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private Asset toDomain(AssetJpaEntity e) {
        return new Asset(e.getId(), e.getSymbol(), e.getName(), e.getAssetType());
    }
}
