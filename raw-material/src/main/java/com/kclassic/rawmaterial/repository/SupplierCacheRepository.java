package com.kclassic.rawmaterial.repository;

import com.kclassic.rawmaterial.entity.RawMaterial;
import com.kclassic.rawmaterial.entity.SupplierCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierCacheRepository extends JpaRepository<SupplierCache, String> {
}
