package com.kclassic.rawmaterial.mapper;

import com.kclassic.rawmaterial.dto.request.RawMaterialRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;
import com.kclassic.rawmaterial.entity.RawMaterial;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RawMaterialMapper {
    RawMaterial toEnity(RawMaterialRequest request);
    RawMaterialResponse toDto(RawMaterial entity);
}
