package com.kclassic.product.dto.response;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ProductDetailsResponse {
    private UUID id;
    private String color;
    private Map<String, Integer> sizes;
    private UUID productId;
    private String productName;
}
