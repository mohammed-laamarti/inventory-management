package com.kclassic.product.repository;

import com.kclassic.product.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, UUID> {
    List<ProductDetails> findByProductId(UUID productId);
}
