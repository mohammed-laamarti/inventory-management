package com.kclassic.product.service;

import com.kclassic.product.dto.request.ProductDetailsRequest;
import com.kclassic.product.dto.response.ProductDetailsResponse;

import java.util.List;
import java.util.UUID;

public interface ProductDetailsService {
    List<ProductDetailsResponse> getAllProductsDetails();
    List<ProductDetailsResponse> getProductDetailsByProductId(UUID productId);
    ProductDetailsResponse createProductDetails(ProductDetailsRequest productDetailsRequest);
    ProductDetailsResponse updateProductDetails(UUID productDetailsId, ProductDetailsRequest productDetailsRequest);
    void deleteProductDetails(UUID productDetailsId);

}
