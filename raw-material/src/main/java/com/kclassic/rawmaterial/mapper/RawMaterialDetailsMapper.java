package com.kclassic.rawmaterial.mapper;

import com.kclassic.rawmaterial.dto.request.RawMaterialDetailsRequest;
import com.kclassic.rawmaterial.dto.response.RawMaterialDetailsResponse;
import com.kclassic.rawmaterial.entity.RawMaterialDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RawMaterialDetailsMapper {

    /**
     * Convertit RawMaterialDetailsRequest en RawMaterialDetails entity
     */
    public RawMaterialDetails toEntity(RawMaterialDetailsRequest request) {
        if (request == null) {
            return null;
        }

        RawMaterialDetails entity = new RawMaterialDetails();
        entity.setQuantity(request.getQuantity());
        entity.setColor(request.getColor());
        entity.setRawMaterialId(request.getRawMaterialId());

        return entity;
    }

    /**
     * Convertit RawMaterialDetails entity en RawMaterialDetailsResponse
     */
    public RawMaterialDetailsResponse toDto(RawMaterialDetails entity) {
        if (entity == null) {
            return null;
        }

        RawMaterialDetailsResponse response = new RawMaterialDetailsResponse();
        response.setId(entity.getId());
        response.setQuantity(entity.getQuantity());
        response.setColor(entity.getColor());
        response.setRawMaterialId(entity.getRawMaterialId());
        response.setUpdatedAt(entity.getUpdatedAt());

        return response;
    }

    /**
     * Convertit une liste de RawMaterialDetails entities en liste de RawMaterialDetailsResponse
     */
    public List<RawMaterialDetailsResponse> toDtoList(List<RawMaterialDetails> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour un RawMaterialDetails entity existant avec les données d'un RawMaterialDetailsRequest
     */
    public void updateEntityFromRequest(RawMaterialDetailsRequest request, RawMaterialDetails entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.getQuantity() != null) {
            entity.setQuantity(request.getQuantity());
        }
        if (request.getColor() != null) {
            entity.setColor(request.getColor());
        }
        if (request.getRawMaterialId() != null) {
            entity.setRawMaterialId(request.getRawMaterialId());
        }
    }
}
