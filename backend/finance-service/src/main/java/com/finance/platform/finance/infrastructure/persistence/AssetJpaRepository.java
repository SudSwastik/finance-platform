package com.finance.platform.finance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface AssetJpaRepository extends JpaRepository<AssetJpaEntity, UUID> {
}
