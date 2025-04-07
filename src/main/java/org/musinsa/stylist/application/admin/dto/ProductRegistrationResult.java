package org.musinsa.stylist.application.admin.dto;

public record ProductRegistrationResult(
    Long productId,
    Long brandId,
    Long categoryId,
    int price
) {

}
