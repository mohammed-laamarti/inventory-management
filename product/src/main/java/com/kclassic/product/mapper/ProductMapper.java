package com.kclassic.product.mapper;

import com.kclassic.product.dto.request.ProductDetailsRequest;
import com.kclassic.product.dto.request.ProductRequest;
import com.kclassic.product.dto.response.ProductDetailsResponse;
import com.kclassic.product.dto.response.ProductResponse;
import com.kclassic.product.entity.Product;
import com.kclassic.product.entity.ProductDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProductEntity(ProductRequest productRequest);

    ProductResponse toProductDto(Product product);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    ProductDetailsResponse toProductDetailsDto(ProductDetails productDetails);

    @Mapping(target = "product", ignore = true)
    ProductDetails toProductDetailsEntity(ProductDetailsRequest productDetailsRequest);
}