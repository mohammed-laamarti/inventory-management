package com.kclassic.product.service.impl;

import com.kclassic.product.dto.request.ProductDetailsRequest;
import com.kclassic.product.dto.response.ProductDetailsResponse;
import com.kclassic.product.entity.Product;
import com.kclassic.product.entity.ProductDetails;
import com.kclassic.product.mapper.ProductMapper;
import com.kclassic.product.repository.ProductDetailsRepository;
import com.kclassic.product.repository.ProductRepository;
import com.kclassic.product.service.ProductDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ProductDetailsServiceImpl implements ProductDetailsService {
    private final ProductRepository productRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDetailsResponse> getAllProductsDetails() {
        return productDetailsRepository.findAll()
                .stream()
                .map(productMapper::toProductDetailsDto)
                .toList();
    }

    @Override
    public List<ProductDetailsResponse> getProductDetailsByProductId(UUID productId) {
        return productDetailsRepository.findByProductId(productId).stream().map(productMapper::toProductDetailsDto).toList();
    }

    @Override
    public ProductDetailsResponse createProductDetails(ProductDetailsRequest request) {
        ProductDetails productDetails = productMapper.toProductDetailsEntity(request);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        productDetails.setProduct(product);

        return productMapper.toProductDetailsDto(productDetailsRepository.save(productDetails));
    }

    @Override
    public ProductDetailsResponse updateProductDetails(UUID productDetailsId, ProductDetailsRequest productDetailsRequest) {
        return productDetailsRepository.findById(productDetailsId)
                .map(existing -> {
                    existing.setColor(productDetailsRequest.getColor());
                    existing.setSizes(productDetailsRequest.getSizes());
                    return productMapper.toProductDetailsDto(productDetailsRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("ProductDetails not found with id " + productDetailsId));
    }

    @Override
    public void deleteProductDetails(UUID productDetailsId) {
        productDetailsRepository.deleteById(productDetailsId);
    }
}
