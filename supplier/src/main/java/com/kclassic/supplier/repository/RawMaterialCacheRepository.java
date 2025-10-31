package com.kclassic.supplier.repository;

import com.kclassic.supplier.entity.RawMaterialCache;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RawMaterialCacheRepository extends MongoRepository<RawMaterialCache, String> {
    // Trouver par rawMaterialId (UUID)
    Optional<RawMaterialCache> findByRawMaterialId(UUID rawMaterialId);

    // Trouver tous les raw materials d'un supplier
    List<RawMaterialCache> findBySupplierId(String supplierId);

    // Supprimer par rawMaterialId
    void deleteByRawMaterialId(UUID rawMaterialId);

    // Compter les raw materials d'un supplier
    long countBySupplierId(String supplierId);
}
