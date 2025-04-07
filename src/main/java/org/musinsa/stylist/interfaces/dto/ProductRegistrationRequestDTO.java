package org.musinsa.stylist.interfaces.dto;

import org.musinsa.stylist.application.admin.dto.ProductRegistrationCommand;

public record ProductRegistrationRequestDTO(
    Long categoryId,
    int price
) {
    public static ProductRegistrationCommand toCommand(ProductRegistrationRequestDTO dto) {
        return new ProductRegistrationCommand(dto.categoryId(), dto.price());
    }
}