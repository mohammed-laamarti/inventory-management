package com.kclassic.rawmaterial.service;

import com.kclassic.rawmaterial.dto.request.RawMaterialRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface RawMaterialService {
    Page<RawMaterialResponse> getRawMaterials(int page, int size);
    RawMaterialResponse getRawMaterial(UUID id);
    RawMaterialResponse saveRawMaterial(RawMaterialRequest request);
    RawMaterialResponse updateRawMaterial(UUID id,RawMaterialRequest request);
    void deleteRawMaterial(UUID id);
}
