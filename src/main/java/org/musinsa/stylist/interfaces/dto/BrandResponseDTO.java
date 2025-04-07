package org.musinsa.stylist.interfaces.dto;

import org.musinsa.stylist.application.admin.dto.BrandRegistrationResult;
import org.musinsa.stylist.domain.model.Brand;

public record BrandResponseDTO(Long brandId, String brandName) {
    public static BrandResponseDTO from(BrandRegistrationResult result) {
        return new BrandResponseDTO(result.brandId(), result.brandName());
    }
}