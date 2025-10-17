package com.kclassic.rawmaterial.service.impl;

import com.kclassic.rawmaterial.dto.request.RawMaterialRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;
import com.kclassic.rawmaterial.entity.RawMaterial;
import com.kclassic.rawmaterial.mapper.RawMaterialMapper;
import com.kclassic.rawmaterial.repository.RawMaterialRepository;
import com.kclassic.rawmaterial.repository.SupplierCacheRepository;
import com.kclassic.rawmaterial.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RawMaterialServiceImpl implements RawMaterialService {

    private final RawMaterialRepository repository;
    private final SupplierCacheRepository supplierCacheRepository;
    private final RawMaterialMapper mapper;

    @Override
    public Page<RawMaterialResponse> getRawMaterials(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .map(mapper::toDto);
    }

    @Override
    public RawMaterialResponse getRawMaterial(UUID id) {
        RawMaterial rawMaterial = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw material not found"));
        return mapper.toDto(rawMaterial);
    }

    @Override
    public RawMaterialResponse saveRawMaterial(RawMaterialRequest request) {
        RawMaterial entity = mapper.toEnity(request);
        entity.setId(UUID.randomUUID());
        RawMaterial saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public RawMaterialResponse updateRawMaterial(UUID id, RawMaterialRequest request) {
        RawMaterial existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw material not found"));

        // Mise à jour des champs
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setImageUrl(request.getImageUrl());
        existing.setUnit(request.getUnit());
        existing.setSupplierId(request.getSupplierId());

        RawMaterial updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteRawMaterial(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Raw material not found");
        }
        repository.deleteById(id);
    }
}
