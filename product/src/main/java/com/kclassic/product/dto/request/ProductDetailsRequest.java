package com.kclassic.product.dto.request;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ProductDetailsRequest {
    private String color;
    private Map<String, Integer> sizes;
    private UUID productId;
}
