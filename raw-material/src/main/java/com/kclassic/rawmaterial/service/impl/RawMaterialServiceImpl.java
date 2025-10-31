package com.kclassic.rawmaterial.service.impl;

import com.kclassic.rawmaterial.dto.request.RawMaterialRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;
import com.kclassic.rawmaterial.entity.RawMaterial;
import com.kclassic.rawmaterial.entity.SupplierCache;
import com.kclassic.rawmaterial.mapper.RawMaterialMapper;
import com.kclassic.rawmaterial.repository.RawMaterialRepository;
import com.kclassic.rawmaterial.repository.SupplierCacheRepository;
import com.kclassic.rawmaterial.service.RawMaterialProducer;
import com.kclassic.rawmaterial.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RawMaterialServiceImpl implements RawMaterialService {

    private final RawMaterialRepository repository;
    private final SupplierCacheRepository supplierCacheRepository;
    private final RawMaterialMapper mapper;
    private final RawMaterialProducer producer; // Injection du Producer

    @Override
    public Page<RawMaterialResponse> getRawMaterials(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .map(rawMaterial -> {
                    RawMaterialResponse response = mapper.toDto(rawMaterial);

                    if (rawMaterial.getSupplierId() != null) {
                        supplierCacheRepository.findById(rawMaterial.getSupplierId())
                                .ifPresent(supplier -> response.setSupplierName(supplier.getName()));
                    }

                    return response;
                });
    }

    @Override
    public RawMaterialResponse getRawMaterial(UUID id) {
        RawMaterial rawMaterial = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw material not found"));

        RawMaterialResponse response = mapper.toDto(rawMaterial);

        // Enrichir avec le nom du supplier
        if (rawMaterial.getSupplierId() != null) {
            supplierCacheRepository.findById(rawMaterial.getSupplierId())
                    .ifPresent(supplier -> response.setSupplierName(supplier.getName()));
        }

        return response;
    }

    @Override
    @Transactional
    public RawMaterialResponse saveRawMaterial(RawMaterialRequest request) {
        log.info("💾 Création d'un nouveau RawMaterial: {}", request.getName());

        RawMaterial entity = mapper.toEntity(request);
        RawMaterial saved = repository.save(entity);
        RawMaterialResponse response = mapper.toDto(saved);

        // Publier l'événement CREATED
        producer.sendRawMaterialCreatedEvent(response);
        log.info("✅ RawMaterial créé: {} - ID: {}", response.getName(), response.getId());

        return response;
    }

    @Override
    @Transactional
    public RawMaterialResponse updateRawMaterial(UUID id, RawMaterialRequest request) {
        log.info("🔄 Mise à jour du RawMaterial: {}", id);

        RawMaterial existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw material not found with id: " + id));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setImageUrl(request.getImageUrl());
        existing.setUnit(request.getUnit());
        existing.setSupplierId(request.getSupplierId());

        RawMaterial updated = repository.save(existing);
        RawMaterialResponse response = mapper.toDto(updated);

        // Publier l'événement UPDATED
        producer.sendRawMaterialUpdatedEvent(response);
        log.info("✅ RawMaterial mis à jour: {} - ID: {}", response.getName(), response.getId());

        return response;
    }

    @Override
    @Transactional
    public void deleteRawMaterial(UUID id) {
        log.info("🗑️ Suppression du RawMaterial: {}", id);

        RawMaterial rawMaterial = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw material not found with id: " + id));

        String supplierId = rawMaterial.getSupplierId();

        repository.deleteById(id);

        // Publier l'événement DELETED
        producer.sendRawMaterialDeletedEvent(id, supplierId);
        log.info("✅ RawMaterial supprimé: {}", id);
    }

    @Override
    public List<SupplierCache> getSuppliers() {
        return supplierCacheRepository.findAll();
    }
}