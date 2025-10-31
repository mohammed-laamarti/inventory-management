package com.kclassic.rawmaterial.service.impl;

import com.kclassic.rawmaterial.dto.request.RawMaterialDetailsRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialDetailsResponse;
import com.kclassic.rawmaterial.entity.RawMaterialDetails;
import com.kclassic.rawmaterial.mapper.RawMaterialDetailsMapper;
import com.kclassic.rawmaterial.repository.RawMaterialDetailsRepository;
import com.kclassic.rawmaterial.repository.SupplierCacheRepository;
import com.kclassic.rawmaterial.service.RawMaterialDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawMaterialDetailsServiceImpl implements RawMaterialDetailsService {

    private final RawMaterialDetailsRepository rawMaterialDetailsRepository;
    private final SupplierCacheRepository supplierCacheRepository;
    private final RawMaterialDetailsMapper rawMaterialDetailsMapper;

    @Override
    public List<RawMaterialDetailsResponse> getRawMaterials() {
        return rawMaterialDetailsRepository.findAll()
                .stream()
                .map(rawMaterialDetailsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RawMaterialDetailsResponse getRawMaterial(UUID id) {
        return rawMaterialDetailsRepository.findById(id)
                .map(rawMaterialDetailsMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Raw material details not found with ID: " + id));
    }

    @Override
    public RawMaterialDetailsResponse saveRawMaterial(RawMaterialDetailsRequest request) {
        RawMaterialDetails entity = rawMaterialDetailsMapper.toEntity(request);
        RawMaterialDetails saved = rawMaterialDetailsRepository.save(entity);
        return rawMaterialDetailsMapper.toDto(saved);
    }

    @Override
    public RawMaterialDetailsResponse updateRawMaterial(UUID id, RawMaterialDetailsRequest request) {
        RawMaterialDetails existing = rawMaterialDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw material details not found with ID: " + id));

        existing.setQuantity(request.getQuantity());
        existing.setColor(request.getColor());
        existing.setRawMaterialId(request.getRawMaterialId());

        RawMaterialDetails updated = rawMaterialDetailsRepository.save(existing);
        return rawMaterialDetailsMapper.toDto(updated);
    }

    @Override
    public void deleteRawMaterial(UUID id) {
        if (!rawMaterialDetailsRepository.existsById(id)) {
            throw new RuntimeException("Raw material details not found with ID: " + id);
        }
        rawMaterialDetailsRepository.deleteById(id);
    }
}
