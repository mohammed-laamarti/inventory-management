package com.kclassic.rawmaterial.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialDetailsRequest {
    private Double quantity;
    private String color;
    private String rawMaterialId;
}
