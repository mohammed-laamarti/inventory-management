package com.kclassic.rawmaterial.dto.response;

import jakarta.persistence.Column;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
public class RawMaterialResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String unit;
    private String supplierId;
    private LocalDateTime updatedAt;
}
