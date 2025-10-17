package com.kclassic.product.service.impl;

import com.kclassic.product.dto.request.ProductRequest;
import com.kclassic.product.dto.response.ProductResponse;
import com.kclassic.product.entity.Product;
import com.kclassic.product.mapper.ProductMapper;
import com.kclassic.product.repository.ProductRepository;
import com.kclassic.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(mapper::toProductDto);
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        return repository.findById(id).map(mapper::toProductDto).orElse(null);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = mapper.toProductEntity(productRequest);
        return mapper.toProductDto(repository.save(product));
    }

    @Override
    public ProductResponse updateProduct(UUID productId, ProductRequest productRequest) {
        Product existingProduct = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID : " + productId));

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setCategory(productRequest.getCategory());
        existingProduct.setBrand(productRequest.getBrand());
        existingProduct.setImageUrl(productRequest.getImageUrl());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setCostPrice(productRequest.getCostPrice());

        Product updated = repository.save(existingProduct);
        return mapper.toProductDto(updated);
    }

    @Override
    public void deleteProduct(UUID productId) {
        repository.deleteById(productId);
    }
}
