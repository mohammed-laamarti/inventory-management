package com.kclassic.product.service;

import com.kclassic.product.dto.request.ProductRequest;
import com.kclassic.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductService {

    Page<ProductResponse> getAllProducts(int page, int size);
    ProductResponse getProductById(UUID id);
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(UUID productId, ProductRequest productRequest);
    void deleteProduct(UUID productId);

}
