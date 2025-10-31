package com.kclassic.product.controller;

import com.kclassic.product.dto.request.ProductDetailsRequest;
import com.kclassic.product.dto.response.ProductDetailsResponse;
import com.kclassic.product.service.ProductDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailsController {

    private final ProductDetailsService productDetailsService;

    @GetMapping
    public List<ProductDetailsResponse> getAllProductDetails() {
        return productDetailsService.getAllProductsDetails();
    }
    @GetMapping("/{id}")
    public List<ProductDetailsResponse> getProductDetailsByProductId(@PathVariable UUID id) {
        return productDetailsService.getProductDetailsByProductId(id);
    }
    @PostMapping
    public ProductDetailsResponse createProductDetails(@RequestBody ProductDetailsRequest productDetailsRequest) {
        return productDetailsService.createProductDetails(productDetailsRequest);
    }
    @PutMapping("/{id}")
    public ProductDetailsResponse updateProductDetails(@PathVariable UUID id, @RequestBody ProductDetailsRequest productDetailsRequest) {
        return productDetailsService.updateProductDetails(id,productDetailsRequest);
    }
    @DeleteMapping("/{id}")
    public void deleteProductDetails(@PathVariable UUID id) {
        productDetailsService.deleteProductDetails(id);
    }

}
