package org.musinsa.stylist.interfaces.dto;

import org.musinsa.stylist.application.admin.dto.ProductUpdateCommand;

public record ProductUpdateRequestDTO(
    Long categoryId,
    int price
) {
    public static ProductUpdateCommand toCommand(ProductUpdateRequestDTO dto) {
        return new ProductUpdateCommand(dto.categoryId(), dto.price());
    }
}