package com.kclassic.rawmaterial.mapper;

import com.kclassic.rawmaterial.dto.request.RawMaterialRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialResponse;
import com.kclassic.rawmaterial.entity.RawMaterial;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RawMaterialMapper {

    /**
     * Convertit RawMaterialRequest en RawMaterial entity
     */
    public RawMaterial toEntity(RawMaterialRequest request) {
        if (request == null) {
            return null;
        }

        RawMaterial entity = new RawMaterial();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setImageUrl(request.getImageUrl());
        entity.setUnit(request.getUnit());
        entity.setSupplierId(request.getSupplierId());

        return entity;
    }

    /**
     * Convertit RawMaterial entity en RawMaterialResponse
     */
    public RawMaterialResponse toDto(RawMaterial entity) {
        if (entity == null) {
            return null;
        }

        return RawMaterialResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .unit(entity.getUnit())
                .supplierId(entity.getSupplierId())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convertit RawMaterial entity en RawMaterialResponse avec le nom du fournisseur
     */
    public RawMaterialResponse toDto(RawMaterial entity, String supplierName) {
        if (entity == null) {
            return null;
        }

        return RawMaterialResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .unit(entity.getUnit())
                .supplierId(entity.getSupplierId())
                .supplierName(supplierName)
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convertit une liste de RawMaterial entities en liste de RawMaterialResponse
     */
    public List<RawMaterialResponse> toDtoList(List<RawMaterial> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour un RawMaterial entity existant avec les données d'un RawMaterialRequest
     */
    public void updateEntityFromRequest(RawMaterialRequest request, RawMaterial entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            entity.setPrice(request.getPrice());
        }
        if (request.getImageUrl() != null) {
            entity.setImageUrl(request.getImageUrl());
        }
        if (request.getUnit() != null) {
            entity.setUnit(request.getUnit());
        }
        if (request.getSupplierId() != null) {
            entity.setSupplierId(request.getSupplierId());
        }
    }
}
