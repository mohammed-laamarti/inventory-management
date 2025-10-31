package com.kclassic.rawmaterial.service;

import com.kclassic.rawmaterial.dto.request.RawMaterialRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;
import com.kclassic.rawmaterial.entity.SupplierCache;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RawMaterialService {
    Page<RawMaterialResponse> getRawMaterials(int page, int size);
    RawMaterialResponse getRawMaterial(UUID id);
    RawMaterialResponse saveRawMaterial(RawMaterialRequest request);
    RawMaterialResponse updateRawMaterial(UUID id,RawMaterialRequest request);
    void deleteRawMaterial(UUID id);
    public List<SupplierCache> getSuppliers();
}
