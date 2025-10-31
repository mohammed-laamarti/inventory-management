package com.kclassic.supplier.dto;

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
}
