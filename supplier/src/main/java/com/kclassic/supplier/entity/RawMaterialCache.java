package com.kclassic.supplier.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "raw_material_cache")
public class RawMaterialCache {

    @Id
    private String id;
    private UUID rawMaterialId;
    private String name;
    private Double price;
    private String imageUrl;
    private String unit;
    private String supplierId;
    private LocalDateTime updatedAt;
}