package com.kclassic.product.mapper;

import com.kclassic.product.dto.request.ProductDetailsRequest;
import com.kclassic.product.dto.request.ProductRequest;
import com.kclassic.product.dto.response.ProductDetailsResponse;
import com.kclassic.product.dto.response.ProductResponse;
import com.kclassic.product.entity.Product;
import com.kclassic.product.entity.ProductDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    /**
     * Convertit ProductRequest en Product entity
     */
    public Product toProductEntity(ProductRequest productRequest) {
        if (productRequest == null) {
            return null;
        }

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setBrand(productRequest.getBrand());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPrice(productRequest.getPrice());
        product.setCostPrice(productRequest.getCostPrice());

        return product;
    }

    /**
     * Convertit Product entity en ProductResponse
     */
    public ProductResponse toProductDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setBrand(product.getBrand());
        response.setImageUrl(product.getImageUrl());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setPrice(product.getPrice());
        response.setCostPrice(product.getCostPrice());

        return response;
    }

    /**
     * Convertit une liste de Product entities en liste de ProductResponse
     */
    public List<ProductResponse> toProductDtoList(List<Product> products) {
        if (products == null) {
            return null;
        }

        return products.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }

    /**
     * Convertit ProductDetails entity en ProductDetailsResponse
     */
    public ProductDetailsResponse toProductDetailsDto(ProductDetails productDetails) {
        if (productDetails == null) {
            return null;
        }

        ProductDetailsResponse response = new ProductDetailsResponse();
        response.setId(productDetails.getId());
        response.setColor(productDetails.getColor());
        response.setSizes(productDetails.getSizes());

        // Mapping du product si présent
        if (productDetails.getProduct() != null) {
            response.setProductId(productDetails.getProduct().getId());
            response.setProductName(productDetails.getProduct().getName());
        }

        return response;
    }

    /**
     * Convertit ProductDetailsRequest en ProductDetails entity
     */
    public ProductDetails toProductDetailsEntity(ProductDetailsRequest productDetailsRequest) {
        if (productDetailsRequest == null) {
            return null;
        }

        ProductDetails productDetails = new ProductDetails();
        productDetails.setColor(productDetailsRequest.getColor());
        productDetails.setSizes(productDetailsRequest.getSizes());
        // Note: Le product sera défini dans le service

        return productDetails;
    }

    /**
     * Convertit une liste de ProductDetails entities en liste de ProductDetailsResponse
     */
    public List<ProductDetailsResponse> toProductDetailsDtoList(List<ProductDetails> productDetailsList) {
        if (productDetailsList == null) {
            return null;
        }

        return productDetailsList.stream()
                .map(this::toProductDetailsDto)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour un Product entity existant avec les données d'un ProductRequest
     */
    public void updateProductFromDto(ProductRequest productRequest, Product product) {
        if (productRequest == null || product == null) {
            return;
        }

        if (productRequest.getName() != null) {
            product.setName(productRequest.getName());
        }
        if (productRequest.getDescription() != null) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getCategory() != null) {
            product.setCategory(productRequest.getCategory());
        }
        if (productRequest.getBrand() != null) {
            product.setBrand(productRequest.getBrand());
        }
        if (productRequest.getImageUrl() != null) {
            product.setImageUrl(productRequest.getImageUrl());
        }
        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }
        if (productRequest.getCostPrice() != null) {
            product.setCostPrice(productRequest.getCostPrice());
        }
    }

    /**
     * Met à jour un ProductDetails entity existant avec les données d'un ProductDetailsRequest
     */
    public void updateProductDetailsFromDto(ProductDetailsRequest request, ProductDetails productDetails) {
        if (request == null || productDetails == null) {
            return;
        }

        if (request.getColor() != null) {
            productDetails.setColor(request.getColor());
        }
        if (request.getSizes() != null) {
            productDetails.setSizes(request.getSizes());
        }
    }
}