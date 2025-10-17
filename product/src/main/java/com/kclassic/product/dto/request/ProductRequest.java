package com.kclassic.product.dto.request;

import lombok.Data;


@Data
public class ProductRequest {
    private String name;
    private String description;
    private String category;
    private String brand;
    private String imageUrl;
    private Double price;
    private Double costPrice;
}
