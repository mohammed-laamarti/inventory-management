package com.kclassic.rawmaterial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialEvent {
    private UUID id;
    private String name;
    private Double price;
    private String imageUrl;
    private String unit;
    private String supplierId;
    private LocalDateTime updatedAt;
    private String eventType;
    public RawMaterialEvent(UUID id, String name, Double price,
                            String imageUrl, String unit, String supplierId,LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.unit = unit;
        this.supplierId = supplierId;
        this.updatedAt = updatedAt;
    }
}
