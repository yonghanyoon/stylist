package org.musinsa.stylist.interfaces.dto;

import org.musinsa.stylist.application.admin.dto.BrandUpdateCommand;

public record BrandUpdateRequestDTO(
    String brandName
) {
    public static BrandUpdateCommand toCommand(BrandUpdateRequestDTO dto) {
        return new BrandUpdateCommand(dto.brandName);
    }
}