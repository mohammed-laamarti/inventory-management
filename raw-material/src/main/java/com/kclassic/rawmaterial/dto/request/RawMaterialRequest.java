package com.kclassic.rawmaterial.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialRequest {

    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String unit;
    private String supplierId;
}
