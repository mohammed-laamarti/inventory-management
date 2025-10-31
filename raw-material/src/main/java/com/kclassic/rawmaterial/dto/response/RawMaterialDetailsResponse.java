package com.kclassic.rawmaterial.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialDetailsResponse {
    private UUID id;
    private Double quantity;
    private String color;
    private String rawMaterialId;
    private LocalDateTime updatedAt;
}
