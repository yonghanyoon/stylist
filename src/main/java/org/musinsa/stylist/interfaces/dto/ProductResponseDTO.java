package org.musinsa.stylist.interfaces.dto;

import org.musinsa.stylist.application.admin.dto.ProductRegistrationResult;

public record ProductResponseDTO(
    Long productId,
    Long brandId,
    Long categoryId,
    int price
) {
    public static ProductResponseDTO from(ProductRegistrationResult result) {
        return new ProductResponseDTO(result.productId(), result.brandId(), result.categoryId(), result.price());
    }
}