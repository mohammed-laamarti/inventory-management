package com.kclassic.rawmaterial.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterialResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String unit;
    private String supplierId;
    private String supplierName;
    private LocalDateTime updatedAt;
}
