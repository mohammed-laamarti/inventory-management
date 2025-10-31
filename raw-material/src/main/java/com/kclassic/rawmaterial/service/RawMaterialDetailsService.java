package com.kclassic.rawmaterial.service;

import com.kclassic.rawmaterial.dto.request.RawMaterialDetailsRequest;
import com.kclassic.rawmaterial.dto.request.RawMaterialRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialDetailsResponse;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;

import java.util.List;
import java.util.UUID;

public interface RawMaterialDetailsService {
    List<RawMaterialDetailsResponse> getRawMaterials();
    RawMaterialDetailsResponse getRawMaterial(UUID id);
    RawMaterialDetailsResponse saveRawMaterial(RawMaterialDetailsRequest request);
    RawMaterialDetailsResponse updateRawMaterial(UUID id,RawMaterialDetailsRequest request);
    void deleteRawMaterial(UUID id);
}
